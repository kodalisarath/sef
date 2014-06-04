package com.ericsson.raso.sef.core.db.service.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.mapper.SubscriberMapper;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;
import com.ericsson.raso.sef.core.db.service.SubscriberService;

public class SubscriberServiceImpl implements SubscriberService {
	private static final Logger logger = LoggerFactory.getLogger(SubscriberServiceImpl.class);
	private SubscriberMapper subscriberMapper;
	
	
	public void setSubscriberMapper(SubscriberMapper subscriberMapper) {
		this.subscriberMapper = subscriberMapper;
	}
	
	@Override
	@Transactional
	public void createSubscriber(Subscriber subscriber) {
		subscriberMapper.createSubscriber(subscriber);
	}

	@Override
	@Transactional
	public void deleteSubscriber(String userId) {
		subscriberMapper.deleteSubscriber(userId);
		evictSubscriber(userId);
	}
	
	@Override
	@Transactional
	public void updateSubscriber(Subscriber subscriber) {
		Subscriber oldSubscriber = subscriberMapper.getSubscriberByUserId(subscriber.getUserId());
		Collection<SubscriberAuditTrial> hists = new ArrayList<SubscriberAuditTrial>();
		if(oldSubscriber.getContractState() != null && oldSubscriber.getContractState() != subscriber.getContractState()) {
			SubscriberAuditTrial history = new SubscriberAuditTrial();
			history.setAttributeName(Subscriber.CONTRACT_STATE);
			history.setAttributeNewValue(subscriber.getContractState().name());
			history.setUserId(subscriber.getUserId());
			history.setEventTimestamp(new Date());
			hists.add(history);
		}
		subscriberMapper.updateSubscriber(subscriber);
		for (SubscriberAuditTrial subscriberHistory : hists) {
			subscriberMapper.insertSubscriberHistory(subscriberHistory);
		}
		
		evictSubscriber(subscriber.getUserId());
	}

	@Override
	@Transactional
	public void setMetas(String userId, Meta... metas) {
		if(metas == null || metas.length == 0) return;
		
		String[] keys = new String[metas.length];
		int i = 0;
		for(Meta meta: metas) {
			keys[i++] = meta.getKey();
		}
		
		Collection<SubscriberAuditTrial> hists = new ArrayList<SubscriberAuditTrial>();
		
		Collection<Meta> oldMetas = this.getMetas(userId, keys);
		for(Meta meta: metas) {
			if(meta.getValue() == null || meta.getValue().trim().length() == 0) {
				continue;
			}

			Meta subMeta = new Meta();
			subMeta.setKey(meta.getKey());
			subMeta.setValue(meta.getValue());
			
			boolean isUpdate = false;
			for(Meta oldmeta: oldMetas) {
				if(subMeta.getKey().equals(oldmeta.getKey())) {
					SubscriberAuditTrial history = new SubscriberAuditTrial();
					history.setAttributeName(meta.getKey());
					history.setAttributeNewValue(meta.getValue());
					history.setUserId(userId);
					history.setEventTimestamp(new Date());
					hists.add(history);
					
					isUpdate = true;
					break;
				}
			}
			
			if(isUpdate) {
				subscriberMapper.updateSubscriberMeta(userId, subMeta, new Date());
			} else {
				SubscriberAuditTrial history = new SubscriberAuditTrial();
				history.setAttributeName(meta.getKey());
				history.setAttributeNewValue(meta.getValue());
				history.setUserId(userId);
				history.setEventTimestamp(new Date());
				hists.add(history);

				subscriberMapper.insertSubscriberMeta(subMeta, new Date(), new Date());
			}
		}
		
		for (SubscriberAuditTrial subscriberHistory : hists) {
			subscriberMapper.insertSubscriberHistory(subscriberHistory);
		}
		
		evictSubscriber(userId);
	}

	@Override
	public Collection<Meta> getMetas(String userId, String... metaKeys) {
		Subscriber subscriber = fetchSubscriberByUserId(userId);
		if(subscriber == null) return Collections.emptyList();
		
		return subscriber.getMetas();
	}

	@Override
	public Subscriber getSubscriber(String msisdn, String... metaKeys) {
		try {
			return fetchSubscriberByMsisdn(msisdn);
		} finally {
		}
	}
	
	@Override
	public Collection<SubscriberAuditTrial> getSubscriberHistory(String userId, String... metaKeys) {
		if(metaKeys == null || metaKeys.length == 0 ) return Collections.emptyList();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("keys", Arrays.asList(metaKeys));
		
		return subscriberMapper.getSubscriberHistory(map);
	}
	
	@Override
	public Subscriber getSubscriberByUserId(String userId) {
		return fetchSubscriberByUserId(userId);
	}
	
	
	private Subscriber fetchSubscriberByMsisdn(String msisdn) {
		Subscriber subscriber = subscriberMapper.getSubscriber(msisdn);
		if(subscriber != null) {
			Collection<Meta> subscriberMetas = fetchMetas(subscriber.getUserId());
			for (Meta meta : subscriberMetas) {
				Meta sMeta = new Meta();
				sMeta.setKey(meta.getKey());
				sMeta.setValue(meta.getValue());
				subscriber.getMetas().add(sMeta);
				subscriber.getMetas().add(new Meta("SUBSCRIBER_ID", subscriber.getUserId()));
			}
		}
		return subscriber;
	}
	
	private Subscriber fetchSubscriberByUserId(String userId) {
		Subscriber subscriber =null;
		try {
			subscriber=subscriberMapper.getSubscriberByUserId(userId);
			if(subscriber != null){
				Collection<Meta> subscriberMetas = fetchMetas(subscriber.getUserId());
				logger.debug("Returned subscriber metas of length for userid:"+subscriber.getUserId() +"is"+ subscriberMetas.size());
				for (Meta meta : subscriberMetas) {
					Meta sMeta = new Meta();
					sMeta.setKey(meta.getKey());
					sMeta.setValue(meta.getValue());
					subscriber.getMetas().add(sMeta);
					subscriber.getMetas().add(new Meta("SUBCRIBER_ID", subscriber.getUserId()));
				}
			}
		} catch (Exception e) {
			logger.debug("Returned a sql error while getting subscriber by userid");
			e.printStackTrace();
			// TODO: handle exception
		}
		return subscriber;
	}
	
	private Collection<Meta> fetchMetas(String userId) {
		return subscriberMapper.getAllSubscriberMetas(userId);
	}

	private void evictSubscriber(String userIdOrMsisdn) {
		
	}

	@Override
	public Subscriber getSubscriber(String msisdn) {
		Subscriber subscriber = null;
		try {
			subscriber = subscriberMapper.getSubscriber(msisdn);
			if (subscriber != null) {
				Collection<Meta> metaCollections = fetchMetas(subscriber
						.getUserId());
				for (Meta meta : metaCollections) {
					Meta sMeta = new Meta();
					sMeta.setKey(meta.getKey());
					sMeta.setValue(meta.getValue());
					subscriber.getMetas().add(sMeta);
					subscriber.getMetas().add(new Meta("SUBSCRIBER_ID", subscriber.getUserId()));
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while querying subscriber entity ",
					e);
		}

		return subscriber;

	}
}

package com.ericsson.raso.sef.core.db.service.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.mapper.SubscriberMapper;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberHistory;
import com.ericsson.raso.sef.core.db.model.SubscriberMeta;
import com.ericsson.raso.sef.core.db.service.SubscriberService;

public class SubscriberServiceImpl implements SubscriberService {
	
	private SubscriberMapper subscriberMapper;
	
	public void setSubscriberMapper(SubscriberMapper subscriberMapper) {
		this.subscriberMapper = subscriberMapper;
	}
	
	@Override
	@Transactional
	public void createSubscriber(Subscriber subscriber) {
		subscriber.setCreated(new DateTime());
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
		Collection<SubscriberHistory> hists = new ArrayList<SubscriberHistory>();
		if(oldSubscriber.getContractState() != null && oldSubscriber.getContractState() != subscriber.getContractState()) {
			SubscriberHistory history = new SubscriberHistory();
			history.setAttributeName(Subscriber.CONTRACT_STATE);
			history.setAttributeNewValue(subscriber.getContractState().name());
			history.setAttributeOldValue(oldSubscriber.getContractState().name());
			history.setUserId(subscriber.getUserId());
			history.setEventTimestamp(new DateTime());
			hists.add(history);
		}
		subscriber.setLastModified(new DateTime());
		subscriberMapper.updateSubscriber(subscriber);
		for (SubscriberHistory subscriberHistory : hists) {
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
		
		Collection<SubscriberHistory> hists = new ArrayList<SubscriberHistory>();
		
		Collection<SubscriberMeta> oldMetas = this.getMetas(userId, keys);
		for(Meta meta: metas) {
			if(meta.getValue() == null || meta.getValue().trim().length() == 0) {
				continue;
			}

			SubscriberMeta subMeta = new SubscriberMeta();
			subMeta.setUserId(userId);
			subMeta.setKey(meta.getKey());
			subMeta.setValue(meta.getValue());
			
			boolean isUpdate = false;
			for(Meta oldmeta: oldMetas) {
				if(subMeta.getKey().equals(oldmeta.getKey())) {
					SubscriberHistory history = new SubscriberHistory();
					history.setAttributeName(meta.getKey());
					history.setAttributeNewValue(meta.getValue());
					history.setAttributeOldValue(oldmeta.getValue());
					history.setUserId(userId);
					history.setEventTimestamp(new DateTime());
					hists.add(history);
					
					isUpdate = true;
					break;
				}
			}
			
			if(isUpdate) {
				subMeta.setLastModified(new DateTime());
				subscriberMapper.updateSubscriberMeta(subMeta);
			} else {
				SubscriberHistory history = new SubscriberHistory();
				history.setAttributeName(meta.getKey());
				history.setAttributeNewValue(meta.getValue());
				history.setAttributeOldValue(null);
				history.setUserId(userId);
				history.setEventTimestamp(new DateTime());
				hists.add(history);

				subMeta.setCreated(new DateTime());
				subMeta.setLastModified(new DateTime());
				subscriberMapper.insertSubscriberMeta(subMeta);
			}
		}
		
		for (SubscriberHistory subscriberHistory : hists) {
			subscriberMapper.insertSubscriberHistory(subscriberHistory);
		}
		
		evictSubscriber(userId);
	}

	@Override
	public Collection<SubscriberMeta> getMetas(String userId, String... metaKeys) {
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
	public Collection<SubscriberHistory> getSubscriberHistory(String userId, String... metaKeys) {
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
			Collection<SubscriberMeta> subscriberMetas = fetchMetas(subscriber.getUserId());
			for (Meta meta : subscriberMetas) {
				SubscriberMeta sMeta = new SubscriberMeta();
				sMeta.setUserId(subscriber.getUserId());
				sMeta.setKey(meta.getKey());
				sMeta.setValue(meta.getValue());
				subscriber.getMetas().add(sMeta);
			}
		}
		return subscriber;
	}
	
	private Subscriber fetchSubscriberByUserId(String userId) {
		Subscriber subscriber = subscriberMapper.getSubscriberByUserId(userId);
		Collection<SubscriberMeta> subscriberMetas = fetchMetas(subscriber.getUserId());
		for (Meta meta : subscriberMetas) {
			SubscriberMeta sMeta = new SubscriberMeta();
			sMeta.setUserId(subscriber.getUserId());
			sMeta.setKey(meta.getKey());
			sMeta.setValue(meta.getValue());
			subscriber.getMetas().add(sMeta);
		}
		
		return subscriber;
	}
	
	private Collection<SubscriberMeta> fetchMetas(String userId) {
		return subscriberMapper.getAllSubscriberMetas(userId);
	}

	private void evictSubscriber(String userIdOrMsisdn) {
		
	}
}

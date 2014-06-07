package com.ericsson.raso.sef.core.db.service.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	public void createSubscriber(Subscriber subscriber) throws Exception {
       
		if(subscriber.getMsisdn() != null){
			subscriber.setPin("1234");
			 if(subscriber.getUserId() == null){
					subscriber.setUserId(subscriber.getMsisdn());
				 }
				 if(subscriber.getAccountId() == null){
					 subscriber.setAccountId(subscriber.getMsisdn());
				 }
				 if(subscriber.getCustomerId() == null){
					 subscriber.setCustomerId(subscriber.getMsisdn());
				 }
		        if(subscriber.getContractId() == null){
					 subscriber.setContractId(subscriber.getMsisdn());
				 }
			 
		 }else{
			 //TO:DO Proper exception handling need to be implimented
			 throw new Exception("MSISDN is null here,cannot process further");
			 
		 }
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
		logger.debug("Inside the update method querying the db for subscriber with userid="+subscriber.getUserId());
		
		Subscriber oldSubscriber = subscriberMapper.getSubscriberByUserId(subscriber.getUserId());
		logger.debug("Query successful for existing subscriber");
		
		logger.debug("Printing existing subscriber fields");
		logger.debug("msisdn= "+oldSubscriber.getMsisdn()+"    "+
		"Account id  ="+oldSubscriber.getAccountId()+"    "+
		"Contractid ="+oldSubscriber.getContractId()+"  "+
		"Contract state  ="+oldSubscriber.getContractState()+" "+
		"Pin   ="+oldSubscriber.getPin()+"  "+"IMSI ="+oldSubscriber.getImsi()+" "+
		"Email ="+oldSubscriber.getEmail()+"  "+"Payment Type  ="+oldSubscriber.getPaymentType()+"   "+
		"Payment Responsible ="+oldSubscriber.getPaymentResponsible()+"  "+"Payment Parent"+
		oldSubscriber.getPaymentParent()+"  "+"Bill life cycle day ="+oldSubscriber.getBillCycleDay()+
		"DOB  ="+oldSubscriber.getDateOfBirth()+"Preferred Language  "+oldSubscriber.getPrefferedLanguage()+
		" Registration date "+oldSubscriber.getRegistrationDate()+" "+"Active Date ="+oldSubscriber.getActiveDate()+
		" Rate Plan="+oldSubscriber.getRatePlan()+"Customer Segment= "+oldSubscriber.getCustomerSegment()+"  IMEI_SV="+oldSubscriber.getImeiSv());
		Collection<SubscriberAuditTrial> hists = new ArrayList<SubscriberAuditTrial>();
		if(oldSubscriber.getContractState() != null && oldSubscriber.getContractState() != subscriber.getContractState()) {
			SubscriberAuditTrial history = new SubscriberAuditTrial();
			history.setAttributeName(Subscriber.CONTRACT_STATE);
			history.setAttributeNewValue(subscriber.getContractState().name());
			history.setUserId(subscriber.getUserId());
			history.setEventTimestamp(new Date());
			hists.add(history);
		}
		
		logger.debug("now going to update subscriber");
		subscriberMapper.updateSubscriber(subscriber);
		
logger.debug("Inside the update method querying the db for subscriber with userid="+subscriber.getUserId());
		
		Subscriber updatedSubscriber = subscriberMapper.getSubscriberByUserId(subscriber.getUserId());
		logger.debug("After updating table ");
		
		logger.debug("Printing Updated subscriber fields");
		logger.debug("msisdn= "+updatedSubscriber.getMsisdn()+"    "+
		"Account id  ="+updatedSubscriber.getAccountId()+"    "+
		"Contractid   ="+updatedSubscriber.getContractId()+"  "+
		"Contract state  ="+updatedSubscriber.getContractState()+" "+
		"Pin   ="+updatedSubscriber.getPin()+"  "+"IMSI ="+updatedSubscriber.getImsi()+" "+
		"Email ="+updatedSubscriber.getEmail()+"  "+"Payment Type  ="+updatedSubscriber.getPaymentType()+"   "+
		"Payment Responsible ="+updatedSubscriber.getPaymentResponsible()+"     "+"Payment Parent"+
		updatedSubscriber.getPaymentParent()+"       "+"Bill life cycle day ="+updatedSubscriber.getBillCycleDay()+
		"DOB  ="+updatedSubscriber.getDateOfBirth()+"     Preferred Language  "+updatedSubscriber.getPrefferedLanguage()+
		" Registration date "+updatedSubscriber.getRegistrationDate()+" "+"Active Date ="+updatedSubscriber.getActiveDate()+
		" Rate Plan="+updatedSubscriber.getRatePlan()+"      Customer Segment= "+updatedSubscriber.getCustomerSegment()+"  IMEI_SV="+updatedSubscriber.getImeiSv());
		
		
		
		
		for (SubscriberAuditTrial subscriberHistory : hists) {
			subscriberMapper.insertSubscriberHistory(subscriberHistory);
		}
		
		
		
		
		evictSubscriber(subscriber.getUserId());
	}

	@Override
	@Transactional
	public void setMetas(String userId, List<Meta> metas) {
		logger.debug("Method setMetas is  called");
		if(metas == null || metas.size() == 0) return;
       		
		String[] keys = new String[metas.size()];
		int i = 0;
		for(Meta meta: metas) {
			keys[i++] = meta.getKey();
		}
		
		Collection<SubscriberAuditTrial> hists = new ArrayList<SubscriberAuditTrial>();
		
		Collection<Meta> oldMetas = this.getMetas(userId, keys);
		logger.debug("old metas values are of size "+oldMetas.size());

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
			logger.debug("checking iff isUpdate is true:   "+isUpdate);
			if(isUpdate) {
				subscriberMapper.updateSubscriberMeta(userId, subMeta, new Date());
			} else {
				logger.debug("Inseting a new Subscriber meta  "+isUpdate);
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
		logger.debug("Extracted fields ");
		
		logger.debug("userid= "+subscriber.getUserId()+"    "+"Account id"+subscriber.getAccountId());
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
		logger.debug("Inside the method to fetch metas");
		return subscriberMapper.getAllSubscriberMetas(userId);
	}

	private void evictSubscriber(String userIdOrMsisdn) {
		
	}

	@Override
	public Subscriber getSubscriber(String msisdn) {
		Subscriber subscriber = null;
		try {
			subscriber = subscriberMapper.getSubscriber(msisdn);
              logger.debug("subscriber returned on calling  a mapper "+subscriber);
			if (subscriber != null) {
				Collection<Meta> metaCollections = fetchMetas(subscriber
						.getUserId());
				logger.debug("Returned metas for the subscriber of size"+metaCollections.size());
				for (Meta meta : metaCollections) {
					Meta sMeta = new Meta();
					if(meta != null){
						sMeta.setKey(meta.getKey());
						sMeta.setValue(meta.getValue());
						subscriber.getMetas().add(sMeta);
					}
					subscriber.getMetas().add(new Meta("SUBSCRIBER_ID", subscriber.getUserId()));
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while querying subscriber entity",
					e);
		}
		return subscriber;

	}
}

package com.ericsson.raso.sef.smart.commons;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.read.EntireRead;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public abstract class SmartServiceHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SmartServiceHelper.class);
	public static EntireRead entireReadSubscriber(String msisdn) throws SmException
	{
		//To DO
		
		return null;
		
	}
	public static SubscriberInfo getAndRefreshSubscriber(String msisdn) {
		
		logger.debug("Entering getAndRefreshSubscriber.....");
		String requestId = RequestContextLocalStore.get().getRequestId();
		logger.debug("Entering Request ID..... " + requestId);
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		
		List<Meta> metas = new ArrayList<Meta>();
		Meta meta = new Meta();
		meta.setKey("READ_SUBSCRIBER");
		meta.setValue("READ_SUBSCRIBER");
		metas.add(meta);
		logger.debug("Entering SmartServiceResolver.....");
		
		
		ISubscriberRequest subscriberRequest = SmartServiceResolver.getSubscriberRequest();
		String correlationId = subscriberRequest.readSubscriber(requestId, msisdn, metas);
		SubscriberResponseStore.put(correlationId, subscriberInfo);
		
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch(InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :"+e.getMessage());
		}
		
		logger.debug("Awake from sleep.. going to check subscriber response in store with id: " +  correlationId);
		
		subscriberInfo = (SubscriberInfo) SubscriberResponseStore.get(correlationId);
		
		updateSubscriberState(subscriberInfo);
		
		return subscriberInfo;
	}
	
	public static void updateSubscriberState(SubscriberInfo subscriberInfo) {
		
		//To DO
		
	}
	
	
	public static SmartModel readSubscriberAccounts(String msisdn)  throws SmException
	{
		//To DO
		
		return null;
		
	}
	
}

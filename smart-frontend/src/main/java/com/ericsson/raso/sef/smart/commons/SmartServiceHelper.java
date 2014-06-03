package com.ericsson.raso.sef.smart.commons;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;

public abstract class SmartServiceHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SmartServiceHelper.class);
	
	public static SubscriberInfo getAndRefreshSubscriber(String msisdn) {
		
		String requestId = RequestContextLocalStore.get().getRequestId();
		
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		
		List<Meta> metas = new ArrayList<Meta>();
		Meta meta = new Meta();
		meta.setKey("READ_SUBSCRIBER");
		meta.setValue("READ_SUBSCRIBER");
		metas.add(meta);
		ISubscriberRequest subscriberRequest = SmartServiceResolver.getSubscriberRequest();
		String correlationId = subscriberRequest.readSubscriber(requestId, msisdn, metas);
		
		try {
			synchronized (subscriberInfo) {
				SubscriberResponseStore.put(requestId, subscriberInfo);
				subscriberInfo.wait(10000L);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		logger.debug("Awake from sleep.. going to check subscriber response in store with id: " +  correlationId);
		
		subscriberInfo = (SubscriberInfo) SubscriberResponseStore.get(correlationId);
		
		updateSubscriberState(subscriberInfo);
		
		return subscriberInfo;
	}
	
	public static void updateSubscriberState(SubscriberInfo subscriberInfo) {
		
	}
	

}
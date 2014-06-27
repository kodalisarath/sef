package com.ericsson.raso.sef.smart.subscription.response;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.HelperConstant;

public class RequestCorrelationStore {
	private static final Logger logger = LoggerFactory.getLogger(RequestCorrelationStore.class);
	//private static Map<String, AbstractSubscriptionResponse> store = new TreeMap<String, AbstractSubscriptionResponse>();
	private static Map<String, AbstractSubscriptionResponse> store = SefCoreServiceResolver.getCloudAwareCluster().getMap(HelperConstant.SMFE_CORRELATION_STORE);
	
	
	public static void put(String requestId, AbstractSubscriptionResponse response) {
		store.put(requestId, response);
	}
	
	public static AbstractSubscriptionResponse get(String requestId) {
		logger.debug("In the static method and returning AbstractSubscriptionResponse,Request id recieved is :: "+requestId);
		return store.get(requestId);
	}
	
	public static AbstractSubscriptionResponse remove(String requestId) {
		return store.remove(requestId);
	}
}

package com.ericsson.raso.sef.smart.subscriber.response;

import java.util.Map;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartConstants;


public class SubscriberResponseStore {

	//private static Map<String, AbstractSubscriberResponse> store = new TreeMap<String, AbstractSubscriberResponse>();
	private static Map<String, AbstractSubscriberResponse> store = SefCoreServiceResolver.getCloudAwareCluster().getMap(SmartConstants.SMFE_CORRELATION_STORE);
	
	public static void put(String requestId, AbstractSubscriberResponse response) {
		store.put(requestId, response);
	}
	
	public static AbstractSubscriberResponse get(String requestId) {
		return store.get(requestId);
	}
	
	public static AbstractSubscriberResponse remove(String requestId) {
		return store.remove(requestId);
	}
}

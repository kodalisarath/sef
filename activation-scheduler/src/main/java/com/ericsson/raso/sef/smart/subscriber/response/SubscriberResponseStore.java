package com.ericsson.raso.sef.smart.subscriber.response;

import java.util.HashMap;
import java.util.Map;


public class SubscriberResponseStore {

	//private static Map<String, AbstractSubscriberResponse> store = new TreeMap<String, AbstractSubscriberResponse>();
	//private static Map<String, AbstractSubscriberResponse> store = SefCoreServiceResolver.getCloudAwareCluster().getMap(HelperConstant.SMFE_CORRELATION_STORE);
	private static Map<String, AbstractSubscriberResponse> store = new HashMap<String, AbstractSubscriberResponse>();
	
	
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

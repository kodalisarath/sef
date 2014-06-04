package com.ericsson.raso.sef.smart.subscription.response;

import java.util.Map;
import java.util.TreeMap;

public class RequestCorrelationStore {
	
	private static Map<String, AbstractSubscriptionResponse> store = new TreeMap<String, AbstractSubscriptionResponse>();
	
	public static void put(String requestId, AbstractSubscriptionResponse response) {
		store.put(requestId, response);
	}
	
	public static AbstractSubscriptionResponse get(String requestId) {
		return store.get(requestId);
	}
	
	public static AbstractSubscriptionResponse remove(String requestId) {
		return store.remove(requestId);
	}
}

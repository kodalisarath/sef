package com.ericsson.raso.sef.bes.engine.transaction.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ericsson.raso.sef.core.Meta;



public interface ISubscriberRequest {
	
	public abstract String readSubscriber(String requestId, String subscriberId, Map<String, String> metas);
	
	public abstract String readSubscriberMeta(String requestId, String subscriberId, Set<String> metaNames);
	
	public abstract String createSubscriber(String requestId, com.ericsson.sef.bes.api.entities.Subscriber subscriber);
	
	public abstract String updateSubscriber(String requestId, String subscriberId, Map<String, String> metas);
	
	public abstract String deleteSubscriber(String requestId, String subscriberId);
	
	public abstract String handleLifeCycle (String requestId, String subscriberId, String lifeCycleState, Map<String, String> metas);

}

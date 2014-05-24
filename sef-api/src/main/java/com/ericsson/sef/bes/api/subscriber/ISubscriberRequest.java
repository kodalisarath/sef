package com.ericsson.sef.bes.api.subscriber;

import java.util.Map;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Subscriber;
@SuppressWarnings("restriction")
@WebService
public interface ISubscriberRequest {
	
	public @WebMethod abstract String readSubscriber(String subscriberId);
	
	public @WebMethod abstract String readSubscriberMeta(String subscriberId, Set<String> metaNames);
	
	public @WebMethod abstract String createSubscriber(Subscriber subscriber);
	
	public @WebMethod abstract String updateSubscriber(String subscriberId, Map<String, String> metas);
	
	public @WebMethod abstract String deleteSubscriber(String subscriberId);
	
	public @WebMethod abstract String handleLifeCycle (String subscriberId, String lifeCycleState, Map<String, String> metas);

}
package com.ericsson.sef.bes.api.subscriber;

import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;

@WebService
public interface ISubscriberRequest {
	
	public @WebMethod abstract String readSubscriber(String requestId, String subscriberId);
	
	public @WebMethod abstract String readSubscriberMeta(String requestId, String subscriberId, Set<String> metaNames);
	
	public @WebMethod abstract String createSubscriber(String requestId, Subscriber subscriber);
	
	public @WebMethod abstract String updateSubscriber(String requestId, String subscriberId, List<Meta> metas);
	
	public @WebMethod abstract String deleteSubscriber(String requestId, String subscriberId);
	
	public @WebMethod abstract String handleLifeCycle (String requestId, String subscriberId, String lifeCycleState, List<Meta> metas);

}
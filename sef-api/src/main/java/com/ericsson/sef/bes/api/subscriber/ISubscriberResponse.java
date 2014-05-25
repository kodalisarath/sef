package com.ericsson.sef.bes.api.subscriber;

import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionException;

@WebService
public interface ISubscriberResponse {
	
	public @WebMethod abstract void readSubscriber(String requestCorrelator, TransactionException fault, Subscriber subscriber);
	
	public @WebMethod abstract void readSubscriberMeta(String requestCorrelator, TransactionException fault, String subscriberId, Map<String, String> metaNames);
	
	public @WebMethod abstract void createSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public @WebMethod abstract void updateSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public @WebMethod abstract void deleteSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public @WebMethod abstract void handleLifeCycle (String requestCorrelator, TransactionException fault, Boolean result);

}
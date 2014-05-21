package com.ericsson.sef.bes.api.subscriber;

import java.util.Map;

import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionException;

public interface ISubscriberResponse {
	
	public abstract void readSubscriber(String requestCorrelator, TransactionException fault, Subscriber subscriber);
	
	public abstract void readSubscriberMeta(String requestCorrelator, TransactionException fault, String subscriberId, Map<String, String> metaNames);
	
	public abstract void createSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract void updateSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract void deleteSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract void handleLifeCycle (String requestCorrelator, TransactionException fault, Boolean result);

}

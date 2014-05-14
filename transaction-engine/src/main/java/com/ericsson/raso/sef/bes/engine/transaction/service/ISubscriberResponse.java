package com.ericsson.raso.sef.bes.engine.transaction.service;

import java.util.Map;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.core.db.model.Subscriber;

public interface ISubscriberResponse {
	
	public abstract void readSubscriber(String requestCorrelator, TransactionException fault, Subscriber subscriber);
	
	public abstract void readSubscriberMeta(String requestCorrelator, TransactionException fault, String subscriberId, Map<String, String> metaNames);
	
	public abstract void createSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract void updateSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract void deleteSubscriber(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract void handleLifeCycle (String requestCorrelator, TransactionException fault, Boolean result);

}

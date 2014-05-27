package com.ericsson.raso.sef.bes.engine.transaction.service;

import java.util.Map;

import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public interface ISubscriberResponse {
	
	public abstract void readSubscriber(String requestCorrelator, TransactionStatus fault, Subscriber subscriber);
	
	public abstract void readSubscriberMeta(String requestCorrelator, TransactionStatus fault, String subscriberId, Map<String, String> metaNames);
	
	public abstract void createSubscriber(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public abstract void updateSubscriber(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public abstract void deleteSubscriber(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public abstract void handleLifeCycle (String requestCorrelator, TransactionStatus fault, Boolean result);

}

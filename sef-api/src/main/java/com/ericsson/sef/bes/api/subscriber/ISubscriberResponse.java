package com.ericsson.sef.bes.api.subscriber;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

@WebService
public interface ISubscriberResponse {
	
	public @WebMethod abstract void readSubscriber(String requestCorrelator, TransactionStatus fault, Subscriber subscriber);
	
	public @WebMethod abstract void readSubscriberMeta(String requestCorrelator, TransactionStatus fault, String subscriberId, List<Meta> metaNames);
	
	public @WebMethod abstract void createSubscriber(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public @WebMethod abstract void updateSubscriber(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public @WebMethod abstract void deleteSubscriber(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public @WebMethod abstract void handleLifeCycle (String requestCorrelator, TransactionStatus fault, Boolean result);

}
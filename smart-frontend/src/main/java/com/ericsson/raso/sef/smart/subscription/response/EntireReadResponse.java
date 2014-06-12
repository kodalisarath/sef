package com.ericsson.raso.sef.smart.subscription.response;

import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.entities.Subscriber;

public class EntireReadResponse extends AbstractSubscriptionResponse {

	private static final long serialVersionUID = 1643475627465673346L;
	private TransactionStatus fault;
	private String subscriptionId;
	private Subscriber subscriber;
	
	public TransactionStatus getFault() {
		return fault;
	}
	public void setFault(TransactionStatus fault) {
		this.fault = fault;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public Subscriber getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
}

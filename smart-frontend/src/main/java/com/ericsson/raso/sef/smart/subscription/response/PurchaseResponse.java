package com.ericsson.raso.sef.smart.subscription.response;

import java.util.Map;

import com.ericsson.sef.bes.api.entities.TransactionException;

public class PurchaseResponse extends AbstractSubscriptionResponse {
	
	private TransactionException fault;
	private String subscriptionId;
	private Map<String, String> billingMetas;
	
	public TransactionException getFault() {
		return fault;
	}
	public void setFault(TransactionException fault) {
		this.fault = fault;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public Map<String, String> getBillingMetas() {
		return billingMetas;
	}
	public void setBillingMetas(Map<String, String> billingMetas) {
		this.billingMetas = billingMetas;
	}
	
	
}

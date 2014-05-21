package com.ericsson.raso.sef.bes.engine.transaction.entities;



public final class HandleSubscriptionEventResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;

	private String subscriptionId = null;
	private Boolean result = null;

	public HandleSubscriptionEventResponse(String requestCorrelator) {
		super(requestCorrelator);
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

		
	
}

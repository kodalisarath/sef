package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class QuerySubscriptionRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private String subscriptionId = null;
	
	public QuerySubscriptionRequest(String requestCorrelator, String offerId) {
		super(requestCorrelator);
		this.subscriptionId = offerId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	

			
}

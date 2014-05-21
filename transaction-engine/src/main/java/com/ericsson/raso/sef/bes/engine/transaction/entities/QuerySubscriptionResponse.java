package com.ericsson.raso.sef.bes.engine.transaction.entities;



public final class QuerySubscriptionResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;

	private Offer result = null;

	public Offer getResult() {
		return result;
	}

	public void setResult(Offer result) {
		this.result = result;
	}

	public QuerySubscriptionResponse(String requestCorrelator) {
		super(requestCorrelator);
	}

		
	
}

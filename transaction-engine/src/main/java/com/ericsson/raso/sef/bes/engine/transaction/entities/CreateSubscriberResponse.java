package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class CreateSubscriberResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;

	private Boolean result = null;

	public CreateSubscriberResponse(String requestCorrelator) {
		super(requestCorrelator);
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	
	
}

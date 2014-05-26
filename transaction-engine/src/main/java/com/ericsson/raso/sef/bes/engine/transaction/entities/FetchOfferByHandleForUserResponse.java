package com.ericsson.raso.sef.bes.engine.transaction.entities;

import com.ericsson.sef.bes.api.entities.Offer;



public final class FetchOfferByHandleForUserResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;

	private String subscriberId = null;
	private Offer result = null;
	
	

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Offer getResult() {
		return result;
	}

	public void setResult(Offer result) {
		this.result = result;
	}

	public FetchOfferByHandleForUserResponse(String requestCorrelator) {
		super(requestCorrelator);
	}

		
	
}

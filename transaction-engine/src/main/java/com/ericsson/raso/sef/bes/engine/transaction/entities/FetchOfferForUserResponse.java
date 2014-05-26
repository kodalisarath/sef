package com.ericsson.raso.sef.bes.engine.transaction.entities;

import com.ericsson.sef.bes.api.entities.Offer;



public final class FetchOfferForUserResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;

	private String subscriberId = null;
	private Offer result = null;
	
	

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String susbcriberId) {
		this.subscriberId = susbcriberId;
	}

	public Offer getResult() {
		return result;
	}

	public void setResult(Offer result) {
		this.result = result;
	}

	public FetchOfferForUserResponse(String requestCorrelator) {
		super(requestCorrelator);
	}

		
	
}

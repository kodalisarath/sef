package com.ericsson.raso.sef.bes.engine.transaction.entities;



public final class FetchOfferByHandleForUserResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;

	private Offer result = null;

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

package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class FetchOfferRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private String offerId = null;
	
	public FetchOfferRequest(String requestCorrelator, String offerId) {
		super(requestCorrelator);
		this.offerId = offerId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

			
}

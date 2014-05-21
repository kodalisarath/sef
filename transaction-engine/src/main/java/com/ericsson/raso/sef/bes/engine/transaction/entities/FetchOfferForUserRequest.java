package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class FetchOfferForUserRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private String offerId = null;
	private String susbcriberId = null;

	public FetchOfferForUserRequest(String requestCorrelator, String offerId, String subscriberId) {
		super(requestCorrelator);
		this.offerId = offerId;
		this.susbcriberId = subscriberId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getSusbcriberId() {
		return susbcriberId;
	}

	public void setSusbcriberId(String susbcriberId) {
		this.susbcriberId = susbcriberId;
	}

	
			
}

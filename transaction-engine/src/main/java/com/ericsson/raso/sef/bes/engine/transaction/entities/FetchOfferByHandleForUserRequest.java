package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class FetchOfferByHandleForUserRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private String handle = null;
	private String susbcriberId = null;

	public FetchOfferByHandleForUserRequest(String requestCorrelator, String handle, String subscriberId) {
		super(requestCorrelator);
		this.handle = handle;
		this.susbcriberId = subscriberId;
	}

	public String getSusbcriberId() {
		return susbcriberId;
	}

	public void setSusbcriberId(String susbcriberId) {
		this.susbcriberId = susbcriberId;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	
	
			
}

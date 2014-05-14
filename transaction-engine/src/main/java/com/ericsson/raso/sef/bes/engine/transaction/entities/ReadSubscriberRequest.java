package com.ericsson.raso.sef.bes.engine.transaction.entities;



public final class ReadSubscriberRequest extends AbstractRequest {
	private static final long	serialVersionUID	= 5310036737033766847L;

	private String subscriberId = null;

	public ReadSubscriberRequest(String requestCorrelator, String subscriberId) {
		super(requestCorrelator);
		this.subscriberId = subscriberId;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}


}

package com.ericsson.raso.sef.bes.engine.transaction.entities;



public final class DeleteSubscriberRequest extends AbstractRequest {
	private static final long	serialVersionUID	= 5113481758520068651L;
	
	private String subscriberId = null;

	public DeleteSubscriberRequest(String requestCorrelator, String subscriberId) {
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

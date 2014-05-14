package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class CreateSubscriberRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private Subscriber subscriber = null;

	public CreateSubscriberRequest(String requestCorrelator, Subscriber subscriber) {
		super(requestCorrelator);
		this.subscriber = subscriber;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public com.ericsson.raso.sef.core.db.model.Subscriber persistableEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}

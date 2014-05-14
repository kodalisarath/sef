package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class ReadSubscriberMetaResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;
	
	private Subscriber	subscriber = null;

	
	public ReadSubscriberMetaResponse(String requestCorrelator, Subscriber subscriber) {
		super(requestCorrelator);
		this.setSubscriber(subscriber);
	}


	public Subscriber getSubscriber() {
		return subscriber;
	}


	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	
	
}

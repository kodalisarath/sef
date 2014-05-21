package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Map;


public final class ReadSubscriberResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;
	
	private Subscriber	subscriber = null;
	private Map<String, String>	metas = null;

	
	public ReadSubscriberResponse(String requestCorrelator) {
		super(requestCorrelator);
	}


	public Subscriber getSubscriber() {
		return subscriber;
	}


	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}


	public Map<String, String> getMetas() {
		return metas;
	}


	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}

	
	
}

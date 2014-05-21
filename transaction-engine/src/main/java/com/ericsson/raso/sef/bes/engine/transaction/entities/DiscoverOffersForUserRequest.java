package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class DiscoverOffersForUserRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private String resource = null;
	private String subscriberId = null;

	public DiscoverOffersForUserRequest(String requestCorrelator, String resource, String subscriberId) {
		super(requestCorrelator);
		this.resource = resource;
		this.subscriberId = subscriberId;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

		
}

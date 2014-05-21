package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class DiscoverOffersRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private String resource = null;

	public DiscoverOffersRequest(String requestCorrelator, String resource) {
		super(requestCorrelator);
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

		
}

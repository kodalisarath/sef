package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Set;

import com.ericsson.sef.bes.api.entities.Offer;


public final class DiscoverOffersForUserResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;

	private Set<Offer> result = null;

	public DiscoverOffersForUserResponse(String requestCorrelator) {
		super(requestCorrelator);
	}

	public Set<Offer> getResult() {
		return result;
	}

	public void setResult(Set<Offer> result) {
		this.result = result;
	}

	
	
}

package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class HandleLifeCycleResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 9103853104666390991L;

	private Boolean result = null;

	public HandleLifeCycleResponse(String requestCorrelator, Boolean result) {
		super(requestCorrelator);
		this.result = result;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	
	
}

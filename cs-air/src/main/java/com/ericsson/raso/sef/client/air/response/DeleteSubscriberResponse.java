package com.ericsson.raso.sef.client.air.response;

public class DeleteSubscriberResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;
	
	private Boolean result;

	public DeleteSubscriberResponse(String requestCorrelator) {
		super();
	}
	
	public Boolean getResult() {
		return result;
	}
	
	public void setResult(Boolean result) {
		this.result = result;
	}
	
}

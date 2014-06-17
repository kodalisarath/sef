package com.ericsson.raso.sef.client.air.request;

public class DeleteSubscriberRequest extends AbstractAirRequest {
	
	private Integer deleteReasonCode;

	public DeleteSubscriberRequest() {
		super("DeleteSubscriber");
	}
	
	public Integer getDeleteReasonCode() {
		return this.deleteReasonCode;
	}
	
	public void setDeleteReasonCode(Integer subscriberNumberNAI) {
		this.deleteReasonCode = deleteReasonCode;
		addParam("deleteReasonCode", this.deleteReasonCode);
	}
	
}

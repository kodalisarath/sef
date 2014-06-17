package com.ericsson.raso.sef.client.air.request;

public class DeleteSubscriberRequest extends AbstractAirRequest {
	
	private Integer deleteReasonCode;
	private String originOperatorID;

	public DeleteSubscriberRequest() {
		super("DeleteSubscriber");
	}
	
	public Integer getDeleteReasonCode() {
		return this.deleteReasonCode;
	}
	
	public void setDeleteReasonCode(Integer deleteReasonCode) {
		this.deleteReasonCode = deleteReasonCode;
		addParam("deleteReasonCode", this.deleteReasonCode);
	}
	
	public void setOriginOperatorID(String originOperatorID){
		this.originOperatorID = originOperatorID;
		addParam("originOperatorID", this.originOperatorID);
	}
	
}

package com.ericsson.raso.sef.fulfillment.commons;

import com.ericsson.raso.sef.core.StatusCode;

public enum FulfillmentResponseCode implements StatusCode {
	FULFILLMENT_INTERNAL_ERROR(50000, "System Internal Error in service fulfilment processing");

	private int code;
	private String message;
	
	private FulfillmentResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}

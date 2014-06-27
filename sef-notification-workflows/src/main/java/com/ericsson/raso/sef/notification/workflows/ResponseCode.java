package com.ericsson.raso.sef.notification.workflows;

import com.ericsson.raso.sef.core.StatusCode;

public enum ResponseCode implements StatusCode {
	
	SUBSCRIBER_GRACE_NOT_ALLOWED(8001,"SUBSCRIBER_GRACE_NOT_ALLOWED"),
	SUBSCRIBER_INSUFFICIENT_BALANCE(8002,"SUBSCRIBER_INSUFFICIENT_BALANCE"),
	SUBSCRIBER_NOT_FOUND(8003,"SUBSCRIBER_NOT_FOUND"),
	SUBSCRIBER_PREACTIVE_NOT_ALLOWED(8004,"SUBSCRIBER_PREACTIVE_NOT_ALLOWED");
	
	private final int code;
	private final String message;
	
	private ResponseCode(final int code, final String message) {
		this.code = code;
		this.message = message;
	}


	public int getCode() {
		return code;
	}


	public String getMessage() {
		return message;
	}
}

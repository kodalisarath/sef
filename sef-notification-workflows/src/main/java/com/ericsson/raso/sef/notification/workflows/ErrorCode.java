package com.ericsson.raso.sef.notification.workflows;

import com.ericsson.raso.sef.core.StatusCode;

public enum ErrorCode implements StatusCode {
	
	success(0, "Successs"),
	invalidRequest(1, "Invalid Request"),
	internalServerError(2,"Internal Service Error"),
	SUBSCRIBER_NOT_FOUND(8003,"SUBSCRIBER_NOT_FOUND");
	
	private final int code;
	private final String message;
	
	private ErrorCode(final int code, final String message) {
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

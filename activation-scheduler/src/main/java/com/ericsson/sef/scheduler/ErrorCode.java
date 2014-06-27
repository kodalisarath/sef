package com.ericsson.sef.scheduler;

import com.ericsson.raso.sef.core.StatusCode;


public enum ErrorCode implements StatusCode {
	
	success(0, "Successs"),
	internalServerError(2,"Internal Service Error");
	
	private final int code;
	private final String message;
	
	private ErrorCode(final int code, final String message) {
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

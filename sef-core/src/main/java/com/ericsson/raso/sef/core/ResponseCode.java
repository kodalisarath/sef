package com.ericsson.raso.sef.core;


public class ResponseCode implements StatusCode {

	private int code;
	private String message;
	
	public ResponseCode(int code, String message) {
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

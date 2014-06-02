package com.ericsson.raso.sef.core;

import java.io.Serializable;


public class ResponseCode implements StatusCode, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7491918730221871928L;
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

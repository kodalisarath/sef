package com.ericsson.raso.sef.charginggateway.diameter;

public enum DiameterErrorCode{
	
	DIAMETER_SUCCESS(2001, "DIAMETER_SUCCESS"),
	DIAMETER_LIMITED_SUCCESS(2002, "DIAMETER_LIMITED_SUCCESS"),
	DIAMETER_TO_BUSY(3004, "DIAMETER_TO_BUSY"),
	DIAMETER_UNKNOWN_SESSION_ID(5002, "DIAMETER_UNKNOWN_SESSION_ID"),
	DIAMETER_UNABLE_TO_COMPLY(5012, "DIAMETER_UNABLE_TO_COMPLY"),
	DIAMETER_TIMEOUT(0000, "DIAMETER_TIMEOUT");

	private int code;
	private String message;
	
	private DiameterErrorCode(int code, String message) {
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

package com.ericsson.raso.sef.logging;

import org.apache.logging.log4j.Marker;

public abstract class SnmpEntity {
	
	protected Marker snmp = null;
	
	private int code = -1;
	private String message = null;
	private String additionalMessage = null;
	

	
	public SnmpEntity(int code, String message) {
		super();
		this.code = code;
		this.message = message;
		this.additionalMessage = null;
	}


	public SnmpEntity(int code, String message, String additionalMessage) {
		super();
		this.code = code;
		this.message = message;
		this.additionalMessage = additionalMessage;
	}


	@Override
	public String toString() {
		return "SnmpEntity [code=" + code + ", message=" + message + ", additionalMessage=" + additionalMessage + "]";
	}
	
	public Marker getSnmpMessage() {
		return this.snmp;
	}


	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getAdditionalMessage() {
		return additionalMessage;
	}


	public void setAdditionalMessage(String additionalMessage) {
		this.additionalMessage = additionalMessage;
	}
	
	

}

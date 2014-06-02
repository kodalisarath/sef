package com.ericsson.raso.sef.core;

public class FrameworkException extends Exception {

	private static final long serialVersionUID = 5793034755089703055L;
	
	public static final StatusCode globalError = new ResponseCode(500, "Internal Server Error");
	
	private String component = "core";
	private StatusCode statusCode = globalError;
	
	public FrameworkException(String component, StatusCode code) {
		this(code.getMessage());
		this.component = component;
		this.statusCode = code;
	}
	
	public FrameworkException(String component, StatusCode code, Throwable e) {
		this(code.getMessage(), e);
		this.component = component;
		this.statusCode = code;
	}

	public FrameworkException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public FrameworkException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FrameworkException(String arg0) {
		super(arg0);
	}

	public FrameworkException(Throwable arg0) {
		super(arg0);
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

}

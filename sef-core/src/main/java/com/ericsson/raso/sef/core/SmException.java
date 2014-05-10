package com.ericsson.raso.sef.core;


public class SmException extends Exception {

	private static final long serialVersionUID = 4191731634512548050L;
	
	public static final StatusCode globalError = new ResponseCode(500, "Internal Server Error");
	public static final StatusCode socketError = new ResponseCode(803, "Socket Time Out");
	
	private final String component;
	private StatusCode statusCode = globalError;

	public SmException(StatusCode code) {
		this("global", code, null);
	}
	
	public SmException(String component,StatusCode code) {
		this(component, code, null);
	}
	
	public SmException(String component,Throwable pLinkedException) {
		this(component, null, pLinkedException);
	}

	public SmException(String component, StatusCode code, Throwable pLinkedException) {
		super(pLinkedException);
		this.component = component;
		if(code != null) {
			this.statusCode = code;
		}
	}
	
	public SmException(Throwable pLinkedException) {
		this("global", pLinkedException);
	}
	
	public String getComponent() {
		return component;
	}
	
	public StatusCode getStatusCode() {
		return statusCode;
	}
	
	@Override
	public String getMessage() {
		if(statusCode != null) {
			return statusCode.getMessage();
		} else {
			return super.getMessage();
		}
	}
}

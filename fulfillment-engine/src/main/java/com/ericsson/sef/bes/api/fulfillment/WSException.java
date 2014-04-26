package com.ericsson.sef.bes.api.fulfillment;

import javax.xml.ws.WebFault;

@WebFault(name = "FaultInfo")
public class WSException extends Exception {

	private static final long serialVersionUID = -7380628399836590104L;
	private FaultInfo wsException;

	public WSException() {
		super();
	}

	public WSException(String message) {
		super(message);
	}

	public WSException(String message, Throwable cause) {
		super(message, cause);
	}

	public WSException(String message, FaultInfo wsException) {
		super(message);
		this.wsException = wsException;
	}

	public WSException(String message, FaultInfo wsException, Throwable cause) {
		super(message, cause);
		this.wsException = wsException;
	}

	public FaultInfo getFaultInfo() {
		return this.wsException;
	}
}

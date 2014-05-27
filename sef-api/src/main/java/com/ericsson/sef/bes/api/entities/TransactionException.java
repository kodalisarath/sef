package com.ericsson.sef.bes.api.entities;

import javax.xml.ws.WebFault;

@WebFault
public final class TransactionException extends Exception {
	private static final long serialVersionUID = 2651520114941007027L;
	
	private String requestId = null;
	
	public TransactionException()
	{
		super();
	}
	
	public TransactionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public TransactionException(String requestId, String arg0) {
		super(arg0);
		this.requestId = requestId;
	}

	public TransactionException(String requestId, String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.requestId = requestId;
	}


	public TransactionException(Throwable arg0) {
		super(arg0);
	}

	protected String getRequestId() {
		return requestId;
	}

	
	
	
}

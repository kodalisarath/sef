package com.ericsson.raso.sef.bes.engine.transaction;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.StatusCode;

public final class TransactionException extends FrameworkException {
	private static final long serialVersionUID = 2651520114941007027L;
	
	private String requestId = null;

	
	public TransactionException(String component, StatusCode code) {
		super(component, code);
	}
	
	public TransactionException(String component, StatusCode code, Throwable e) {
		super(component, code, e);
	}
	
	public TransactionException(String requestId, String arg0) {
		super(arg0);
		this.requestId = requestId;
	}

	public TransactionException(String requestId, String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.requestId = requestId;
	}


	public TransactionException(String requestId, Throwable arg0) {
		super(arg0);
	}

	protected String getRequestId() {
		return requestId;
	}

	@Override
	public String toString() {
		return "TransactionException [requestId=" + requestId + "]";
	}

	
	
	
}

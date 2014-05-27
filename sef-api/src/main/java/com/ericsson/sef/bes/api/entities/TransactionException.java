package com.ericsson.sef.bes.api.entities;

import javax.xml.ws.WebFault;

/* @WebFault(faultBean = "com.ericsson.sef.bes.api.entities.FaultInfo") */
public final class TransactionException extends Exception {
	private static final long serialVersionUID = 2651520114941007027L;
	
	private FaultInfo faultBean;
	private String requestId = null;
	
	public TransactionException(String message, FaultInfo faultBean) {
		super(message);
		this.faultBean = faultBean;
	}
	
	public TransactionException(String message, FaultInfo faultBean, Throwable cause) {
		super(message);
		this.faultBean = faultBean;
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

	
	public FaultInfo getFaultInfo() {
		return faultBean;
	}
	
//    @Override
//    public StackTraceElement[] getStackTrace() {
//        return super.getStackTrace();
//    }
//	
//    @Override
//    public Throwable getCause() {
//        return super.getCause();
//    }	
}

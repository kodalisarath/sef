package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.AbstractRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.AbstractResponse;

public abstract class AbstractTransaction implements Callable<Void>, Serializable {
	private static final long serialVersionUID = -7350766184775285546L;

	private String requestId = null;
	private AbstractRequest request = null;
	private AbstractResponse response = null;

	public AbstractTransaction(String requestId, AbstractRequest request) {
		super();
		this.requestId = requestId;
		this.request = request;
	}

	/**
	 * This method represents the execution unit part of the Command Pattern. To keep with consistency of design, a request and response
	 * pojo is provided in the class. The execution unit must produce a concrete response and set the AbstractResponse in the base class.
	 * 
	 * 
	 * @throws TransactionException
	 */
	public abstract Void execute() throws TransactionException;

	@Override
	public Void call() throws Exception {
		if (this.request == null)
			throw new TransactionException(this.requestId, "Transaction not initialized properly to execute");

		return this.execute();
	}

	public AbstractRequest getRequest() {
		return request;
	}

	public AbstractResponse getResponse() {
		return response;
	}

	protected void setRequest(AbstractRequest request) {
		this.request = request;
	}

	protected void setResponse(AbstractResponse response) {
		this.response = response;
	}

	public String getRequestId() {
		return requestId;
	}
	
	

}

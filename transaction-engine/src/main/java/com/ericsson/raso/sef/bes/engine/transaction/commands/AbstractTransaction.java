package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.AbstractRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.AbstractResponse;
import com.ericsson.raso.sef.core.ResponseCode;

public abstract class AbstractTransaction implements Callable<Boolean>, Serializable {
	private static final long serialVersionUID = -7350766184775285546L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTransaction.class);

	private String requestId = null;
	private AbstractRequest request = null;
	private AbstractResponse response = null;
	private Map<String, String>	metas;

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
	public abstract Boolean execute() throws TransactionException;
	
	public abstract void sendResponse();

	@Override
	public Boolean call() throws Exception {
		if (this.request == null)
			this.response.setReturnFault(new TransactionException(this.requestId, "Transaction not initialized properly to execute"));

		try {
			this.execute();
			//this.sendResponse(); //-- not all the time, the response will be blocking execution. In cases, the use-case may request orchestration who will invoke sendResponse()
			return true;
		} catch (Throwable e) {
			LOGGER.error("Use-case: " + this.getClass().getName() + ":" + this.requestId, e);
			
			if (e instanceof TransactionException) {
				this.response.setReturnFault((TransactionException) e);
				this.sendResponse();
				return false;
			}
			
			if (e instanceof RuntimeException) {
				LOGGER.error("Abnormal flow - " + e.getClass().getName() + ": " + e.getMessage());
				this.response.setReturnFault(new TransactionException("txe", new ResponseCode(999, "Transaction failed"), e));
				this.sendResponse();
				return false;
			}
				
			if (e instanceof Error) {
				LOGGER.error("Container under Stress - " + e.getClass().getName() + ": " + e.getMessage());
				this.response.setReturnFault(new TransactionException("txe", new ResponseCode(999, "System under stress..."), e));
				this.sendResponse();
				throw e;
			} else {
				LOGGER.error("Gneric Exception Handler - " + e.getClass().getName() + ": " + e.getMessage());
				this.response.setReturnFault(new TransactionException("txe", new ResponseCode(999, "System under stress..."), e));
				this.sendResponse();
				return false;
			}
		}
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

	public void setMetas(Map<String, String> metas) {
		LOGGER.debug("Metas received from Orchestration: " + this.metas);
		this.metas = metas;
	}

	
	public Map<String, String> getMetas() {
		return metas;
	}
	
	

}

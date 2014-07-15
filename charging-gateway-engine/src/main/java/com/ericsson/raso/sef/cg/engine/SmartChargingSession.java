package com.ericsson.raso.sef.cg.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.raso.sef.cg.engine.Operation.Type;

public class SmartChargingSession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String msisdn;
	private String messageId;
	private String sessionId;
	private Operation operation;
	private String hostId;
	private TransactionStatus transactionStatus;
	private long creationTime = System.currentTimeMillis();
	
	private Map<Type, List<Avp>> requestAvpMap = new HashMap<Type, List<Avp>>();
	private Map<Type, List<Avp>> responseAvpMap = new HashMap<Type, List<Avp>>();
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sourceSessionId) {
		this.sessionId = sourceSessionId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation currentOperation) {
		this.operation = currentOperation;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public String getMsisdn() {
		return msisdn;
	}
	
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	public List<Avp> getRequestAvp(Type type) {
		return requestAvpMap.get(type);
	}
	public void addRequestAvp(Type type, List<Avp> avps) {
		requestAvpMap.put(type, avps);
	}
	
	public List<Avp> getResponseAvp(Type type) {
		return responseAvpMap.get(type);
	}
	
	public void addResponseAvp(Type type, List<Avp> avps) {
		responseAvpMap.put(type, avps);
	}
	
	public long getCreationTime() {
		return creationTime;
	}
}

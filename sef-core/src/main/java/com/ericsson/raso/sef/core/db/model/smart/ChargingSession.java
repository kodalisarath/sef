package com.ericsson.raso.sef.core.db.model.smart;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.raso.sef.core.cg.model.Operation;
import com.ericsson.raso.sef.core.cg.model.Operation.Type;
import com.ericsson.raso.sef.core.cg.model.TransactionStatus;

public class ChargingSession implements Serializable {


	@Override
	public String toString() {
		return "ChargingSession [msisdn=" + msisdn + ", messageId=" + messageId
				+ ", sessionId=" + sessionId + ", operation=" + operation
				+ ", hostId=" + hostId + ", transactionStatus="
				+ transactionStatus + ", creationTime=" + creationTime
				+ ", requestAvpMap=" + requestAvpMap + ", responseAvpMap="
				+ responseAvpMap + ", requestAVPMapInJSONFormat="
				+ requestAVPMapInJSONFormat + ", responseAvpMapInJSONFormat="
				+ responseAvpMapInJSONFormat + "]";
	}

	private static final long serialVersionUID = 1L;

	private String msisdn;

	private String messageId;

	private String sessionId;

	private Operation operation;

	private String hostId;

	private TransactionStatus transactionStatus;

	private Date creationTime = new Date() ;
	
	//private long expiryTime =0;



	private Map<Type, List<Avp>> requestAvpMap = new HashMap<Type, List<Avp>>();

	private Map<Type, List<Avp>> responseAvpMap = new HashMap<Type, List<Avp>>();
	
	private String requestAVPMapInJSONFormat ;
	private String responseAvpMapInJSONFormat;
	

	public String getResponseAvpMapInJSONFormat() {
		return responseAvpMapInJSONFormat;
	}

	public void setResponseAvpMapInJSONFormat(String responseAvpMapInJSONFormat) {
		this.responseAvpMapInJSONFormat = responseAvpMapInJSONFormat;
	}

	public String getSessionId() {

		return sessionId;

	}

	public Map<Type, List<Avp>> getRequestAvpMap() {
		return requestAvpMap;
	}

	public void setRequestAvpMap(Map<Type, List<Avp>> requestAvpMap) {
		this.requestAvpMap = requestAvpMap;
	}

	public Map<Type, List<Avp>> getResponseAvpMap() {
		return responseAvpMap;
	}

	public void setResponseAvpMap(Map<Type, List<Avp>> responseAvpMap) {
		this.responseAvpMap = responseAvpMap;
	}

	public String getRequestAVPMapInJSONFormat() {
		return requestAVPMapInJSONFormat;
	}

	public void setRequestAVPMapInJSONFormat(String requestAVPMapInJSONFormat) {
		this.requestAVPMapInJSONFormat = requestAVPMapInJSONFormat;
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

	public Date getCreationTime() {

		return creationTime;

	}

/*	public long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}*/

	/*public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}*/
}

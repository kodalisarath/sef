package com.ericsson.raso.sef.client.air.request;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcRequest;

public abstract class AbstractAirRequest extends XmlRpcRequest {

	public AbstractAirRequest(String methodName) {
		super(methodName);
		paramMap = new HashMap<String, Object>();
		super.addParam(paramMap);
	}

	private String originNodeType;
	private String originHostName;
	private String originTransactionId;
	private Date originTimeStamp = new Date();
	private String subscriberNumber;
	private String originOperatorId;
	private Integer subscriberNumberNAI;
	private String siteId;
	
	private Map<String, Object> paramMap;
	
	protected void addParam(String key, Object value) {
		paramMap.put(key, value);
	}

	public String getOriginNodeType() {
		return originNodeType;
	}

	public void setOriginNodeType(String originNodeType) {
		this.originNodeType = originNodeType;
		addParam("originNodeType", this.originNodeType);
	}

	public String getOriginHostName() {
		return originHostName;
	}

	public void setOriginHostName(String originHostName) {
		this.originHostName = originHostName;
		addParam("originHostName", this.originHostName);
	}

	public String getOriginTransactionId() {
		return originTransactionId;
	}

	public void setOriginTransactionId(String originTransactionId) {
		this.originTransactionId = originTransactionId;
		addParam("originTransactionID", this.originTransactionId);
	}

	public Date getOriginTimeStamp() {
		return originTimeStamp;
	}

	public void setOriginTimeStamp(Date originTimeStamp) {
		this.originTimeStamp = originTimeStamp;
		addParam("originTimeStamp", this.originTimeStamp);
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
		addParam("subscriberNumber", this.subscriberNumber);
	}

	public String getOriginOperatorId() {
		return originOperatorId;
	}

	public void setOriginOperatorId(String originOperatorId) {
		this.originOperatorId = originOperatorId;
		addParam("originOperatorID", this.originOperatorId);
	}
	
	public Integer getSubscriberNumberNAI() {
		return subscriberNumberNAI;
	}
	
	public void setSubscriberNumberNAI(Integer subscriberNumberNAI) {
		this.subscriberNumberNAI = subscriberNumberNAI;
		addParam("subscriberNumberNAI", this.subscriberNumberNAI);
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
}

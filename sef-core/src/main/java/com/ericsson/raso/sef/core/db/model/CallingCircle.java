package com.ericsson.raso.sef.core.db.model;

import org.joda.time.DateTime;

public class CallingCircle {

	private long id;
	private String aparty;
	private String bparty;
	private String iLProductId;
	private CallingCircleRelation relationship;
	private String purchaseReference;
	private DateTime expiryTime;
	private DateTime creationTime;
	private Boolean deleted;
	private transient String apartyMsisdn;
	private transient String bpartyMsisdn;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAparty() {
		return aparty;
	}

	public void setAparty(String aparty) {
		this.aparty = aparty;
	}

	public String getBparty() {
		return bparty;
	}

	public void setBparty(String bparty) {
		this.bparty = bparty;
	}

	public String getiLProductId() {
		return iLProductId;
	}

	public void setiLProductId(String iLProductId) {
		this.iLProductId = iLProductId;
	}

	public CallingCircleRelation getRelationship() {
		return relationship;
	}

	public void setRelationship(CallingCircleRelation relation) {
		this.relationship = relation;
	}

	public String getPurchaseReference() {
		return purchaseReference;
	}

	public void setPurchaseReference(String purchaseReference) {
		this.purchaseReference = purchaseReference;
	}

	public DateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(DateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getApartyMsisdn() {
		return apartyMsisdn;
	}

	public void setApartyMsisdn(String apartyMsisdn) {
		this.apartyMsisdn = apartyMsisdn;
	}

	public String getBpartyMsisdn() {
		return bpartyMsisdn;
	}

	public void setBpartyMsisdn(String bpartyMsisdn) {
		this.bpartyMsisdn = bpartyMsisdn;
	}
}

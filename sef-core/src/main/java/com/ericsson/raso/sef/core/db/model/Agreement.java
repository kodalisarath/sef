package com.ericsson.raso.sef.core.db.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class Agreement {

	private String agreementId;
	private String providerId;
	private String agreementName;
	private String agreementType;
	private Date createdTime;
	private Date lastModified;
	private String state;
	
	Collection<AgreementMeta> agreementMetas;
	
	public String getAgreementId() {
		return agreementId;
	}
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getAgreementName() {
		return agreementName;
	}
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}
	public String getAgreementType() {
		return agreementType;
	}
	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Collection<AgreementMeta> getAgreementMetas() {
		if(agreementMetas==null){
			agreementMetas = new ArrayList<AgreementMeta>();
		}
		return agreementMetas;
	}
	public void setAgreementMetas(Collection<AgreementMeta> agreementMetas) {
		this.agreementMetas = agreementMetas;
	}
}

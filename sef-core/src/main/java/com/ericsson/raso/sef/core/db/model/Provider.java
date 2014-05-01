package com.ericsson.raso.sef.core.db.model;

import org.joda.time.DateTime;

public class Provider {

	private String providerId;
	private String partnerName;
	private String password;
	private DateTime createdTime;
	private DateTime lastModified;
	private String state;
	private String status;
	private Integer loginCount;
	private DateTime passwordModified;

	

	public DateTime getPasswordModified() {
		return passwordModified;
	}

	public void setPasswordModified(DateTime passwordModified) {
		this.passwordModified = passwordModified;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(DateTime createdTime) {
		this.createdTime = createdTime;
	}

	public DateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(DateTime lastModified) {
		this.lastModified = lastModified;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}

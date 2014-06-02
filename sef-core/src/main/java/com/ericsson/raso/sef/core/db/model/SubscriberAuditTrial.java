package com.ericsson.raso.sef.core.db.model;

import org.joda.time.DateTime;

public class SubscriberAuditTrial {

	private String userId;
	private DateTime eventTimestamp;
	private String attributeName;
	private String attributeNewValue;
	private String changedByUser;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public DateTime getEventTimestamp() {
		return eventTimestamp;
	}

	public void setEventTimestamp(DateTime eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeNewValue() {
		return attributeNewValue;
	}

	public void setAttributeNewValue(String attributeNewValue) {
		this.attributeNewValue = attributeNewValue;
	}

	public String getChangedByUser() {
		return changedByUser;
	}

	public void setChangedByUser(String changedByUser) {
		this.changedByUser = changedByUser;
	}

}

package com.ericsson.raso.sef.core.db.model;

import org.joda.time.DateTime;

public class SubscriberHistory {

	private String userId;
	private DateTime eventTimestamp;
	private String attributeName;
	private String attributeOldValue;
	private String attributeNewValue;
	private String changeByProviderId;
	private String changeByServiceId;

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

	public String getAttributeOldValue() {
		return attributeOldValue;
	}

	public void setAttributeOldValue(String attributeOldValue) {
		this.attributeOldValue = attributeOldValue;
	}

	public String getAttributeNewValue() {
		return attributeNewValue;
	}

	public void setAttributeNewValue(String attributeNewValue) {
		this.attributeNewValue = attributeNewValue;
	}

	public String getChangeByProviderId() {
		return changeByProviderId;
	}

	public void setChangeByProviderId(String changeByProviderId) {
		this.changeByProviderId = changeByProviderId;
	}

	public String getChangeByServiceId() {
		return changeByServiceId;
	}

	public void setChangeByServiceId(String changeByServiceId) {
		this.changeByServiceId = changeByServiceId;
	}

}

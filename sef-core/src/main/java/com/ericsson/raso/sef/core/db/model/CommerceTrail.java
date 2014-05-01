package com.ericsson.raso.sef.core.db.model;

import org.joda.time.DateTime;

public class CommerceTrail {

	private String eventId; //unique Key
	private String userId;  //unique Key
	private DateTime eventTimestamp;
	private EventType eventType;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public DateTime getEventTimestamp() {
		return eventTimestamp;
	}

	public void setEventTimestamp(DateTime eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}

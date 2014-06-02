package com.ericsson.raso.sef.core.db.model;

import org.joda.time.DateTime;

import com.ericsson.raso.sef.core.Meta;

public class SubscriberMeta extends Meta {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3270466466601612918L;
	private String userId;
	private DateTime created;
	private DateTime lastModified;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public DateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(DateTime lastModified) {
		this.lastModified = lastModified;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}
}

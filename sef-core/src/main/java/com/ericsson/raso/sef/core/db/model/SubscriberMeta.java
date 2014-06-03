package com.ericsson.raso.sef.core.db.model;

import java.util.Date;

import com.ericsson.raso.sef.core.Meta;

public class SubscriberMeta extends Meta {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3270466466601612918L;
	private String userId;
	private Date created;
	private Date lastModified;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}

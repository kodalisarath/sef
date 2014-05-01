package com.ericsson.raso.sef.core.db.model;

import com.ericsson.raso.sef.core.Meta;

public class ScheduledRequestMeta extends Meta {

	private long scheduledRequestId;

	public long getScheduledRequestId() {
		return scheduledRequestId;
	}

	public void setScheduledRequestId(long scheduledRequestId) {
		this.scheduledRequestId = scheduledRequestId;
	}
}

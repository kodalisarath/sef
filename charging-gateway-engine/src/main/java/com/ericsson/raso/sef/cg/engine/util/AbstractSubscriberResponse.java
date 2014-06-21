package com.ericsson.raso.sef.cg.engine.util;

import java.io.Serializable;

public class AbstractSubscriberResponse implements Serializable {

	private static final long serialVersionUID = 8705746084826405500L;

	private String requestId = null;
	private long requestedTime = 0l;
	private long timeElapsed = 0l;
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public long getRequestedTime() {
		return requestedTime;
	}
	public void setRequestedTime(long requestedTime) {
		this.requestedTime = requestedTime;
	}
	public long getTimeElapsed() {
		return timeElapsed;
	}
	public void setTimeElapsed(long timeElapsed) {
		this.timeElapsed = timeElapsed;
	}
	
}

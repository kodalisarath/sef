package com.ericsson.raso.sef.smart.subscription.response;

public class AbstractSubscriptionResponse {

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

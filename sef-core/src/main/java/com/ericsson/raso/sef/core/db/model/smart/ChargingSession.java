package com.ericsson.raso.sef.core.db.model.smart;

import java.util.Date;


public class ChargingSession {

	private String sessionId;
	private String sessionInfo;
	private Date creationTime;
	private Date expiryTime;
	
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionInfo() {
		return sessionInfo;
	}
	public void setSessionInfo(String sessionInfo) {
		this.sessionInfo = sessionInfo;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
	
	@Override
	public String toString() {
		return "ChargingSession [sessionId=" + sessionId + ", sessionInfo=" + sessionInfo + ", creationTime=" + creationTime
				+ ", expiryTime=" + expiryTime + "]";
	}
	
	
	
	
}

package com.ericsson.raso.sef.core.db.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class ScheduledRequest {

	private long id;
	private String userId;
	private String msisdn;
	private String jobId;
	private String requestId;
	private String offerId;
	private String resourceId;
	private String purchaseId;
	private SubscriptionLifeCycleEvent lifeCycleEvent;
	private DateTime created;
	private DateTime updated;
	private DateTime recurrentTime;
	private DateTime expiryTime;
	private DateTime scheduleTime;
	private ScheduledRequestStatus status;
	
	private List<ScheduledRequestMeta> requestMetas = new ArrayList<ScheduledRequestMeta>();
	
	public long getId() {
		return id;
	}
	
	public DateTime getRecurrentTime() {
		return recurrentTime;
	}

	public DateTime getExpiryTime() {
		return expiryTime;
	}

	public void setRecurrentTime(DateTime recurrentTime) {
		this.recurrentTime = recurrentTime;
	}

	public void setExpiryTime(DateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public SubscriptionLifeCycleEvent getLifeCycleEvent() {
		return lifeCycleEvent;
	}

	public void setLifeCycleEvent(SubscriptionLifeCycleEvent lifeCycleEvent) {
		this.lifeCycleEvent = lifeCycleEvent;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public List<ScheduledRequestMeta> getRequestMetas() {
		return requestMetas;
	}

	public void setRequestMetas(List<ScheduledRequestMeta> requestMetas) {
		this.requestMetas = requestMetas;
	}

	public String getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public DateTime getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(DateTime scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public ScheduledRequestStatus getStatus() {
		return status;
	}

	public void setStatus(ScheduledRequestStatus status) {
		this.status = status;
	}

	public DateTime getUpdated() {
		return updated;
	}

	public void setUpdated(DateTime updated) {
		this.updated = updated;
	}
}

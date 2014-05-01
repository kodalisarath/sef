package com.ericsson.raso.sef.core.db.model;

import java.math.BigDecimal;

public class JobDetail {

	private String jobName;
	private String jobClassName;
	private byte[] jobData;
	
	private String triggerName;
	private BigDecimal nextFireTime;
	private BigDecimal startTime;
	private int repeatCount;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public byte[] getJobData() {
		return jobData;
	}

	public void setJobData(byte[] jobData) {
		this.jobData = jobData;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public BigDecimal getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(BigDecimal nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public BigDecimal getStartTime() {
		return startTime;
	}

	public void setStartTime(BigDecimal startTime) {
		this.startTime = startTime;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
}

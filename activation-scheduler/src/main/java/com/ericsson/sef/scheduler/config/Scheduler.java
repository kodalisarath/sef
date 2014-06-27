package com.ericsson.sef.scheduler.config;

public class Scheduler extends Endpoint {
	
	private boolean allowJobExecution;
	private String jdbcEndpointId;

	public boolean isAllowJobExecution() {
		return allowJobExecution;
	}

	public void setAllowJobExecution(boolean allowJobExecution) {
		this.allowJobExecution = allowJobExecution;
	}

	public String getJdbcEndpointId() {
		return jdbcEndpointId;
	}

	public void setJdbcEndpointId(String jdbcEndpointId) {
		this.jdbcEndpointId = jdbcEndpointId;
	}

}

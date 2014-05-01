package com.ericsson.raso.sef.core.db.model;


public class ProviderSLARequest {

	private String providerName;
	private String allowedRequests;

	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getAllowedRequests() {
		return allowedRequests;
	}
	public void setAllowedRequests(String allowedRequests) {
		this.allowedRequests = allowedRequests;
	}

}

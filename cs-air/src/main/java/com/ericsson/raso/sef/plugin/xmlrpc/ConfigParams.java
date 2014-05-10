package com.ericsson.raso.sef.plugin.xmlrpc;


public class ConfigParams {
	String url;
	String username;
	String password;
	String userAgent;
	int connectionTimeout;
	int maxTotalConnections;
	int maxConnectionsPerHost;
	boolean staleCheckingEnabled;
	int idleConnctionTimeout;
	int idleConnectionTimeoutInterval;
	int retryCount;
	boolean useApacheHttpClient;
	int SoTimeout;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public boolean isStaleCheckingEnabled() {
		return staleCheckingEnabled;
	}

	public void setStaleCheckingEnabled(boolean staleCheckingEnabled) {
		this.staleCheckingEnabled = staleCheckingEnabled;
	}

	public int getIdleConnctionTimeout() {
		return idleConnctionTimeout;
	}

	public void setIdleConnctionTimeout(int idleConnctionTimeout) {
		this.idleConnctionTimeout = idleConnctionTimeout;
	}

	public int getIdleConnectionTimeoutInterval() {
		return idleConnectionTimeoutInterval;
	}

	public void setIdleConnectionTimeoutInterval(int idleConnectionTimeoutInterval) {
		this.idleConnectionTimeoutInterval = idleConnectionTimeoutInterval;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public boolean isUseApacheHttpClient() {
		return useApacheHttpClient;
	}

	public void setUseApacheHttpClient(boolean useApacheHttpClient) {
		this.useApacheHttpClient = useApacheHttpClient;
	}

	public int getSoTimeout() {
		return SoTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		SoTimeout = soTimeout;
	}
}
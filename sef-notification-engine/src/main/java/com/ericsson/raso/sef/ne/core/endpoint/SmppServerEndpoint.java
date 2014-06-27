package com.ericsson.raso.sef.ne.core.endpoint;



public class SmppServerEndpoint extends Endpoint {

	public SmppServerEndpoint() {
		this.setProtocol(Protocol.smpp);
		this.setProtocolType(ProtocolType.server);
	}
	
	private int port;
private int bindTimeout;
	private String systemId;
	 private String password;
 private int maxConnectionSize;
 private int defaultWindowSize;
 private int defaultWindowWaitTimeout;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBindTimeout() {
		return bindTimeout;
	}

	public void setBindTimeout(int bindTimeout) {
		this.bindTimeout = bindTimeout;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public int getMaxConnectionSize() {
		return maxConnectionSize;
	}

	public void setMaxConnectionSize(int maxConnectionSize) {
		this.maxConnectionSize = maxConnectionSize;
	}

	public int getDefaultWindowSize() {
		return defaultWindowSize;
	}

	public void setDefaultWindowSize(int defaultWindowSize) {
		this.defaultWindowSize = defaultWindowSize;
	}

	public int getDefaultWindowWaitTimeout() {
		return defaultWindowWaitTimeout;
	}

	public void setDefaultWindowWaitTimeout(int defaultWindowWaitTimeout) {
		this.defaultWindowWaitTimeout = defaultWindowWaitTimeout;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}

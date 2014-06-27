package com.ericsson.raso.sef.ne.core.endpoint;

import com.ericsson.raso.sef.ne.core.config.SourceSmppAddress;
import com.ericsson.raso.sef.ne.core.config.TargetSmppAddress;

public class SmppClientEndpoint extends Endpoint {

	public SmppClientEndpoint() {
		this.setProtocol(Protocol.smpp);
		this.setProtocolType(ProtocolType.client);
	}

	//IConfig config = SefCoreServiceResolver.getConfigService();
	//String destIp = config.getValue("Smsc_Props", "targetHost");
	private String host;
	private int port ;
	private String systemId;
	private String systemType;
	private String password;
	private int windowSize;
	private long requestExpiryTimeout;
	private long windowMonitorInterval;
	private boolean countersEnabled;
	private int maximumRedeliveries = 1;
	private long redeliveryDelay;
	private long bindTimeout;
	private long connectionTimeout;

	private SourceSmppAddress sourceAddress;
	private TargetSmppAddress destinationAddress;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public long getRequestExpiryTimeout() {
		return requestExpiryTimeout;
	}

	public void setRequestExpiryTimeout(long requestExpiryTimeout) {
		this.requestExpiryTimeout = requestExpiryTimeout;
	}

	public long getWindowMonitorInterval() {
		return windowMonitorInterval;
	}

	public void setWindowMonitorInterval(long windowMonitorInterval) {
		this.windowMonitorInterval = windowMonitorInterval;
	}

	public boolean isCountersEnabled() {
		return countersEnabled;
	}

	public void setCountersEnabled(boolean countersEnabled) {
		this.countersEnabled = countersEnabled;
	}

	public int getMaximumRedeliveries() {
		return maximumRedeliveries;
	}

	public void setMaximumRedeliveries(int maximumRedeliveries) {
		this.maximumRedeliveries = maximumRedeliveries;
	}

	public long getRedeliveryDelay() {
		return redeliveryDelay;
	}

	public void setRedeliveryDelay(long redeliveryDelay) {
		this.redeliveryDelay = redeliveryDelay;
	}

	public long getBindTimeout() {
		return bindTimeout;
	}

	public void setBindTimeout(long bindTimeout) {
		this.bindTimeout = bindTimeout;
	}

	public SourceSmppAddress getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(SourceSmppAddress sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public TargetSmppAddress getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(TargetSmppAddress destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
}

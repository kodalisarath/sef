package com.ericsson.raso.sef.core.cg.diameter;

import java.util.List;

public class Diameter extends Endpoint {

	private String stackUri;
	private String realm;
	private String ipAddress;
	private String productId;
	private String firmwareRevision;
	private boolean duplicateRequestDetectionActive;
	private boolean acceptUnknownPeers;
	private long vendorId;
	private long accountId;
	private long authId;
	private long messageTimeout;
	private String fqdn;
	private int ownTcpPort;
	private int poolSize;
	private int eventQueueSize;
	private int sendQueueSize;
	private int sendMessageLimit;
	private String ethInterface;

	private List<StaticRoute> staticRoutes;

	public String getStackUri() {
		if (stackUri != null) {
			return stackUri;
		}
		String ip = SmCoreUtil.getServerIP(getEthInterface());
		String stkUri = "aaa://" + ip + ":" + getOwnTcpPort()
				+ ";transport=tcp";
		return stkUri;
	}

	public void setStackUri(String stackUri) {
		
		this.stackUri = stackUri;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getIpAddress() {
		return SmCoreUtil.getServerIP(getEthInterface());
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getFirmwareRevision() {
		return firmwareRevision;
	}

	public void setFirmwareRevision(String firmwareRevision) {
		this.firmwareRevision = firmwareRevision;
	}

	public boolean isDuplicateRequestDetectionActive() {
		return duplicateRequestDetectionActive;
	}

	public void setDuplicateRequestDetectionActive(
			boolean duplicateRequestDetectionActive) {
		this.duplicateRequestDetectionActive = duplicateRequestDetectionActive;
	}

	public boolean isAcceptUnknownPeers() {
		return acceptUnknownPeers;
	}

	public void setAcceptUnknownPeers(boolean acceptUnknownPeers) {
		this.acceptUnknownPeers = acceptUnknownPeers;
	}

	public long getVendorId() {
		return vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getAuthId() {
		return authId;
	}

	public void setAuthId(long authId) {
		this.authId = authId;
	}

	public long getMessageTimeout() {
		return messageTimeout;
	}

	public void setMessageTimeout(long messageTimeout) {
		this.messageTimeout = messageTimeout;
	}

	public String getFqdn() {
		if (fqdn != null) {
			return fqdn;
		}
		String ip = SmCoreUtil.getServerIP(getEthInterface()).split("\\.")[3];
		String fqdnUri = "il" + ip + "." + getRealm();
		return fqdnUri;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public int getOwnTcpPort() {
		return ownTcpPort;
	}

	public void setOwnTcpPort(int ownTcpPort) {
		this.ownTcpPort = ownTcpPort;
	}

	public List<StaticRoute> getStaticRoutes() {
		return staticRoutes;
	}

	public void setStaticRoutes(List<StaticRoute> staticRoutes) {
		this.staticRoutes = staticRoutes;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getEventQueueSize() {
		return eventQueueSize;
	}

	public void setEventQueueSize(int eventQueueSize) {
		this.eventQueueSize = eventQueueSize;
	}

	public int getSendQueueSize() {
		return sendQueueSize;
	}

	public void setSendQueueSize(int sendQueueSize) {
		this.sendQueueSize = sendQueueSize;
	}

	public int getSendMessageLimit() {
		return sendMessageLimit;
	}

	public void setSendMessageLimit(int sendMessageLimit) {
		this.sendMessageLimit = sendMessageLimit;
	}

	public String getEthInterface() {
		return ethInterface;
	}

	public void setEthInterface(String ethInterface) {
		this.ethInterface = ethInterface;
	}
}
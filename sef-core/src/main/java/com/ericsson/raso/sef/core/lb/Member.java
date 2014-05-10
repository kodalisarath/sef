package com.ericsson.raso.sef.core.lb;

public class Member {

	private String realm;
	private String address;
	private String hostId;
	private String siteId;
	private boolean defaultSite;
	
	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public boolean isDefaultSite() {
		return defaultSite;
	}

	public void setDefaultSite(boolean defaultSite) {
		this.defaultSite = defaultSite;
	}

}

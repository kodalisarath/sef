package com.ericsson.sm.client.af.request;

public class DeleteDnsRequest {

	private String zname;
	private String msisdn;
	private int dclass;
	private long ttl;
	private int dtype;
	private String siteId;

	public String getZname() {
		return zname;
	}

	public void setZname(String zname) {
		this.zname = zname;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public int getDclass() {
		return dclass;
	}

	public void setDclass(int dclass) {
		this.dclass = dclass;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public int getDtype() {
		return dtype;
	}

	public void setDtype(int dtype) {
		this.dtype = dtype;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
}

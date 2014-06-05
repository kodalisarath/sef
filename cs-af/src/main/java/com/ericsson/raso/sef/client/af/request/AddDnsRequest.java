package com.ericsson.raso.sef.client.af.request;

public class AddDnsRequest {

	private String zname;
	private String msisdn;
	private String rdata;
	private int dclass;
	private long ttl;
	private int dtype;
	private String sdpId;
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

	public String getRdata() {
		return rdata;
	}

	public void setRdata(String rdata) {
		this.rdata = rdata;
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
	
	public String getSdpId() {
		return sdpId;
	}
	
	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dclass;
		result = prime * result + dtype;
		result = prime * result + ((msisdn == null) ? 0 : msisdn.hashCode());
		result = prime * result + ((rdata == null) ? 0 : rdata.hashCode());
		result = prime * result + ((sdpId == null) ? 0 : sdpId.hashCode());
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
		result = prime * result + (int) (ttl ^ (ttl >>> 32));
		result = prime * result + ((zname == null) ? 0 : zname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddDnsRequest other = (AddDnsRequest) obj;
		if (dclass != other.dclass)
			return false;
		if (dtype != other.dtype)
			return false;
		if (msisdn == null) {
			if (other.msisdn != null)
				return false;
		} else if (!msisdn.equals(other.msisdn))
			return false;
		if (rdata == null) {
			if (other.rdata != null)
				return false;
		} else if (!rdata.equals(other.rdata))
			return false;
		if (sdpId == null) {
			if (other.sdpId != null)
				return false;
		} else if (!sdpId.equals(other.sdpId))
			return false;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		if (ttl != other.ttl)
			return false;
		if (zname == null) {
			if (other.zname != null)
				return false;
		} else if (!zname.equals(other.zname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "<AddDnsRequest zname='" + zname + "'> <msisdn='" + msisdn + "'> <rdata='" + rdata + "'> <dclass='" + dclass + "'> <ttl='"
				+ ttl + "'> <dtype='" + dtype + "'> <sdpId='" + sdpId + "'> <siteId='" + siteId + "/> </AddDnsRequest>";
	}
	
	
	
}

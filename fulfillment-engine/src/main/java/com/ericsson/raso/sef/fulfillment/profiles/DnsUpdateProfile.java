package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.List;
import java.util.Map;

import com.ericsson.sef.bes.api.entities.Product;

public class DnsUpdateProfile extends BlockingFulfillment<com.ericsson.sef.bes.api.entities.Product> {

	protected DnsUpdateProfile(String name) {
		super(name);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String zname;
	private String rdata;
	private int dtype;
	private int dclass;
	private int ttl;

	public String getZname() {
		return zname;
	}

	public void setZname(String zname) {
		this.zname = zname;
	}

	public String getRdata() {
		return rdata;
	}

	public void setRdata(String rdata) {
		this.rdata = rdata;
	}

	public int getDtype() {
		return dtype;
	}

	public void setDtype(int dtype) {
		this.dtype = dtype;
	}

	public int getDclass() {
		return dclass;
	}

	public void setDclass(int dclass) {
		this.dclass = dclass;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dclass;
		result = prime * result + dtype;
		result = prime * result + ((rdata == null) ? 0 : rdata.hashCode());
		result = prime * result + ttl;
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
		DnsUpdateProfile other = (DnsUpdateProfile) obj;
		if (dclass != other.dclass)
			return false;
		if (dtype != other.dtype)
			return false;
		if (rdata == null) {
			if (other.rdata != null)
				return false;
		} else if (!rdata.equals(other.rdata))
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
		return "DnsUpdateProfile [zname=" + zname + ", rdata=" + rdata
				+ ", dtype=" + dtype + ", dclass=" + dclass + ", ttl=" + ttl
				+ "]";
	}
	
}

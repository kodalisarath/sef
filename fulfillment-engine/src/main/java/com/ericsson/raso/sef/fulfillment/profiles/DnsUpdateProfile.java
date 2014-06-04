package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.af.command.AddDnsCommand;
import com.ericsson.raso.sef.client.af.command.DeleteDnsCommand;
import com.ericsson.raso.sef.client.af.request.AddDnsRequest;
import com.ericsson.raso.sef.client.af.request.DeleteDnsRequest;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.bes.api.entities.Product;

public class DnsUpdateProfile extends BlockingFulfillment<com.ericsson.sef.bes.api.entities.Product> {
	private static final long	serialVersionUID	= -6927140819818049080L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DnsUpdateProfile.class);

	public DnsUpdateProfile(String name) {
		super(name);
	}

	
	private String zname;
	private String rdata;
	private int dtype;
	private int dclass;
	private int ttl;

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) {
		LOGGER.debug("Starting to fulfill installing subscriber in CS-AF...");
		
		DnsUpdateProfile dnsUpdateProfile = new DnsUpdateProfile(zname);
		AddDnsRequest dnsRequest = new AddDnsRequest();
		dnsRequest.setMsisdn(map.get("msisdn"));
		dnsRequest.setDclass(dnsUpdateProfile.getDclass());
		dnsRequest.setDtype(dnsUpdateProfile.getDtype());
		dnsRequest.setRdata(dnsUpdateProfile.getRdata());
		dnsRequest.setTtl(dnsUpdateProfile.getTtl());
		dnsRequest.setZname(dnsUpdateProfile.getZname());
		
		String sdpId= (String) RequestContextLocalStore.get().getInProcess().get("sdpId");
		dnsRequest.setSdpId(sdpId);
		
		try {
			new AddDnsCommand(dnsRequest).execute();
		} catch (SmException e1) {
			LOGGER.error("Failed AddDnsCommand execute" + e1);
		}
		LOGGER.debug("Installed new subscriber in CS-AF DNS");
		 
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;	
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		LOGGER.debug("Starting rollback of CS-AF installed subscriber...");
		
		DnsUpdateProfile dnsUpdateProfile=new DnsUpdateProfile(zname);
		DeleteDnsRequest deleteDnsRequest = new DeleteDnsRequest();
		deleteDnsRequest.setMsisdn(map.get("msisdn"));
		deleteDnsRequest.setDclass(dnsUpdateProfile.getDclass());
		deleteDnsRequest.setDtype(dnsUpdateProfile.getDtype());
		deleteDnsRequest.setSiteId(null);
		deleteDnsRequest.setTtl(dnsUpdateProfile.getTtl());
		deleteDnsRequest.setZname(dnsUpdateProfile.getZname());
		
		try {
			new DeleteDnsCommand(deleteDnsRequest).execute();
		} catch (SmException e1) {
			LOGGER.error("SmException while calling DeleteDnsCommand execute" + e1);
		}
		LOGGER.debug("Installed subcriber in CS-AF rolledback...");
		
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;
	}

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

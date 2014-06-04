package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.client.af.command.AddDnsCommand;
import com.ericsson.raso.sef.client.af.command.DeleteDnsCommand;
import com.ericsson.raso.sef.client.af.request.AddDnsRequest;
import com.ericsson.raso.sef.client.af.request.DeleteDnsRequest;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SmException;
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
		DnsUpdateProfile dnsUpdateProfile=new DnsUpdateProfile(zname);
		AddDnsRequest dnsRequest = new AddDnsRequest();
		
		dnsRequest.setMsisdn(map.get("msisdn"));
		dnsRequest.setDclass(dnsUpdateProfile.getDclass());
		dnsRequest.setDtype(dnsUpdateProfile.getDtype());
		dnsRequest.setRdata(dnsUpdateProfile.getRdata());
		dnsRequest.setTtl(dnsUpdateProfile.getTtl());
		dnsRequest.setZname(dnsUpdateProfile.getZname());
		
		String sdpId= (String) RequestContextLocalStore.get().getInProcess().get("sdpId");
		//requestContext.get("sdpId");
		dnsRequest.setSdpId(sdpId);
		
		try {
			new AddDnsCommand(dnsRequest).execute();
		} catch (SmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

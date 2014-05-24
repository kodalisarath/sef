package com.ericsson.raso.sef.client.af.internal;

public class DnsAddress {
	
	private String ip;
	
	//Defaulted to use UDP
	private boolean useTcp = false;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public boolean isUseTcp() {
		return useTcp;
	}
	public void setUseTcp(boolean useTcp) {
		this.useTcp = useTcp;
	}
}

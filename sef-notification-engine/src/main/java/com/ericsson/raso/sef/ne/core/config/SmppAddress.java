package com.ericsson.raso.sef.ne.core.config;


public class SmppAddress {

	 private byte ton;
	 private byte npi;
	 private String address;

	public SmppAddress() {
		this((byte) 0, (byte) 0, (String) null);
	}

	public SmppAddress(String address) {
		this((byte) 0, (byte) 0,address);
	}
	
	public SmppAddress(byte ton, byte npi, String address) {
		this.ton = ton;
		this.npi = npi;
		this.address = address;
	}

	public byte getTon() {
		return this.ton;
	}

	public void setTon(byte value) {
		this.ton = value;
	}

	public byte getNpi() {
		return this.npi;
	}

	public void setNpi(byte value) {
		this.npi = value;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String value) {
		this.address = value;
	}
}

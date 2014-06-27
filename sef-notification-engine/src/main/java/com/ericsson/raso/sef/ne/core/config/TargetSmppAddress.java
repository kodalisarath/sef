package com.ericsson.raso.sef.ne.core.config;

public class TargetSmppAddress extends SmppAddress {
	public TargetSmppAddress(String address)
	{
		super(address);
	}
	
	public TargetSmppAddress(byte ton, byte npi, String address) {
		super(ton,npi,address);
	}
}

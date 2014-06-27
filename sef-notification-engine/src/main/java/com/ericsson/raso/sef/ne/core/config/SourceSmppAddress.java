package com.ericsson.raso.sef.ne.core.config;

public class SourceSmppAddress extends SmppAddress {

	public SourceSmppAddress(String address)
	{
		super(address);
	}
	
	public SourceSmppAddress(byte ton, byte npi, String address) {
		super(ton,npi,address);
	}
}

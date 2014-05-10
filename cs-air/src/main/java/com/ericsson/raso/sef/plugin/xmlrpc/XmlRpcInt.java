package com.ericsson.raso.sef.plugin.xmlrpc;

public class XmlRpcInt {
	
	private String value;

	public XmlRpcInt(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}

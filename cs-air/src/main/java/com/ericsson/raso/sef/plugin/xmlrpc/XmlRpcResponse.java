package com.ericsson.raso.sef.plugin.xmlrpc;

public abstract class XmlRpcResponse {
	
	protected Object result;
	
	public void setResult(Object result) {
		this.result =  result;
	}
}

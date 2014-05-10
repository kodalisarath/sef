package com.ericsson.raso.sef.plugin.xmlrpc;

import java.util.ArrayList;
import java.util.Collection;

public abstract class XmlRpcRequest {
	
	public XmlRpcRequest(String methodName) {
		this.methodName = methodName;
		params = new ArrayList<Object>();
	}

	private String methodName;
	private Collection<Object> params;
	
	public String getMethodName() {
		return this.methodName;
	}
	
	public Object[] getParams() {
		return params.toArray();
	}
	
	public void addParam(Object param) {
		params.add(param);
	}
}

package com.ericsson.raso.sef.plugin.xmlrpc.internal;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactoryImpl;

public class SmHttpTransportFactory extends XmlRpcTransportFactoryImpl {
	
	private int retryCount;
	
	public SmHttpTransportFactory(XmlRpcClient pClient, int retryCount) {
		super(pClient);
		this.retryCount = retryCount;
	}
	
	public XmlRpcTransport getTransport() {
	    return new SmXmlHttpTransport(getClient(), retryCount);
	}
}

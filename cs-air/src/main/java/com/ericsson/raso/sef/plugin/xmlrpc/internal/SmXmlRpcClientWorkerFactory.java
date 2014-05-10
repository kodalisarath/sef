package com.ericsson.raso.sef.plugin.xmlrpc.internal;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientWorker;
import org.apache.xmlrpc.client.XmlRpcClientWorkerFactory;
import org.apache.xmlrpc.common.XmlRpcLoadException;
import org.apache.xmlrpc.common.XmlRpcWorker;

public class SmXmlRpcClientWorkerFactory extends XmlRpcClientWorkerFactory {

	public SmXmlRpcClientWorkerFactory(XmlRpcClient pClient) {
		super(pClient);
	}
	
	@Override
	public XmlRpcWorker getWorker() throws XmlRpcLoadException {
		return new XmlRpcClientWorker(this);
	}
	
	@Override
	protected XmlRpcWorker newWorker() {
		return new XmlRpcClientWorker(this);
	}
	
	@Override
	public void releaseWorker(XmlRpcWorker pWorker) {
		///Do nothing
	}
}

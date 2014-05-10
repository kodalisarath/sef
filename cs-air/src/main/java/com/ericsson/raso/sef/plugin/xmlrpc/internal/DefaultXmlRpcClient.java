package com.ericsson.raso.sef.plugin.xmlrpc.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcClient;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcRequest;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcResponse;

public class DefaultXmlRpcClient implements XmlRpcClient {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultXmlRpcClient.class);

	private org.apache.xmlrpc.client.XmlRpcClient nativeClient;

	public void setNativeClient(org.apache.xmlrpc.client.XmlRpcClient nativeClient) {
		this.nativeClient = nativeClient;
	}

	@Override
	public <R extends XmlRpcRequest, S extends XmlRpcResponse> void execute(R request, S response)
			throws XmlRpcException {
		try {
			Object result = nativeClient.execute(request.getMethodName(), request.getParams());
			response.setResult(result);
		} catch (org.apache.xmlrpc.XmlRpcException e) {
			logger.error(e.getMessage(), e);
			if(e.linkedException != null) {
				logger.error(e.linkedException.getMessage(), e.linkedException);
			}
			throw new XmlRpcException(e.code, e.getMessage(), e.linkedException);
		}
	}

}

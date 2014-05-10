package com.ericsson.raso.sef.plugin.xmlrpc;


public interface XmlRpcClient {
	
	<R extends XmlRpcRequest,S extends XmlRpcResponse> void execute(R request, S response) throws XmlRpcException;
}

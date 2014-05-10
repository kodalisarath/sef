package com.ericsson.raso.sef.plugin.xmlrpc;

public interface XmlRpcClientFactory {

	XmlRpcClient create(ConfigParams params) throws XmlRpcException;

}

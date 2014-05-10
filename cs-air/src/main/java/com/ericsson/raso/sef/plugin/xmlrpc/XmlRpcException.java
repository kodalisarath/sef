package com.ericsson.raso.sef.plugin.xmlrpc;


public class XmlRpcException extends Exception {

	private static final long serialVersionUID = 1L;

	public final int code;
	public final Throwable linkedException;

	public XmlRpcException(int pCode, String pMessage) {
		this(pCode, pMessage, null);
	}

	public XmlRpcException(String pMessage, Throwable pLinkedException) {
		this(0, pMessage, pLinkedException);
	}

	public XmlRpcException(String pMessage) {
		this(0, pMessage, null);
	}

	public XmlRpcException(int pCode, String pMessage, Throwable pLinkedException) {
		super(pMessage);
		code = pCode;
		linkedException = pLinkedException;
	}
}

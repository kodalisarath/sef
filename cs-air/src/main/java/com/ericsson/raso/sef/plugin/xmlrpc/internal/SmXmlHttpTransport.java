package com.ericsson.raso.sef.plugin.xmlrpc.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcHttpClientConfig;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class SmXmlHttpTransport extends XmlRpcSunHttpTransport {
	
	private static Logger logger = LoggerFactory.getLogger(SmXmlHttpTransport.class);
	
	private int retryCount;

	public SmXmlHttpTransport(XmlRpcClient pClient, int retryCount) {
		super(pClient);
		this.retryCount = retryCount > 0 ? retryCount + 1 : 1;
	}
	
	@Override
	public Object sendRequest(XmlRpcRequest pRequest) throws XmlRpcException {
		XmlRpcHttpClientConfig config = (XmlRpcHttpClientConfig) pRequest.getConfig();
		for (int i = 1; i <= retryCount; i++) {
			try {
				return super.sendRequest(pRequest);
			} catch (XmlRpcException e) {
				logger.error(e.getMessage(), e);
				if (e.linkedException != null && e.linkedException instanceof SocketException) {
					logger.error("Recreating the connection..");
					try {
						destroy();
					} catch (Exception e1) {
						//Eat me
					}
					try {
						newURLConnection(config.getServerURL());
					} catch (IOException e1) {
						//Eat me
					}
				} else {
					throw e;
				}
			}
		}
		throw new XmlRpcException("Exausted with retries. Number of retries= " + (retryCount-1));
	}
	
	private void destroy() {
		((HttpURLConnection)getURLConnection()).disconnect();
	}
	
	@Override
	protected void close() throws XmlRpcClientException {
		final HttpURLConnection c = (HttpURLConnection) getURLConnection();
		try {
			InputStream err = c.getErrorStream();
			 if (err != null) {
	                byte buffer[] = new byte[1024];
	                while ((err.read(buffer)) > 0) {
	                }
	                err.close();
	            } 
		} catch (IOException e) {
			//ignore
		}
		try {
			InputStream in = c.getInputStream();
			 if (in != null) {
	                byte buffer[] = new byte[1024];
	                while ((in.read(buffer)) > 0) {
	                }
	                in.close();
	            } 
		} catch (IOException e) {
			//ignore
		}
		try {
			c.getOutputStream().close();
		} catch (IOException e) {
			//ignore
		}
	}
	
	@Override
	protected InputStream getInputStream() throws XmlRpcException {
		if (logger.isDebugEnabled()) {
			InputStream istream = super.getInputStream();
			return new ByteArrayInputStream(CustomLoggingUtils.logResponse(logger, istream).getBytes());
		}
		return super.getInputStream();
	}

	@Override
	protected void writeRequest(final ReqWriter pWriter) throws XmlRpcException, IOException, SAXException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pWriter.write(baos);
		if (logger.isDebugEnabled()) {
			CustomLoggingUtils.logRequest(logger, baos);
		}
		super.writeRequest(pWriter);
	}
}

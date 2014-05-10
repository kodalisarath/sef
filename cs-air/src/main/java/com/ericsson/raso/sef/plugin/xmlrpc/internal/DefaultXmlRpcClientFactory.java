package com.ericsson.raso.sef.plugin.xmlrpc.internal;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.xmlrpc.client.XmlRpcClientConfig;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.common.XmlRpcWorkerFactory;

import com.ericsson.raso.sef.plugin.xmlrpc.ConfigParams;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcClient;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcClientFactory;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class DefaultXmlRpcClientFactory implements XmlRpcClientFactory {

	@Override
	public XmlRpcClient create(ConfigParams params) throws XmlRpcException {
		DefaultXmlRpcClient client = null;
		try {
			System.out.println("HTTP max connections:" + System.getProperty("http.maxConnections"));
			org.apache.xmlrpc.client.XmlRpcClient xmlRpcClient = new org.apache.xmlrpc.client.XmlRpcClient();

			XmlRpcClientConfig xmlRpcConfig = createConfig(params);
			xmlRpcClient.setConfig(xmlRpcConfig);
			
			SmTypeFactory factory = new SmTypeFactory(xmlRpcClient);
			xmlRpcClient.setTypeFactory(factory);
			
			if(params.isUseApacheHttpClient()) {
				XmlRpcCommonsTransportFactory xmlRpcCommonsTransportFactory = new CustomXmlRpcCommonsTransportFactory(xmlRpcClient);
				HttpClient httpClient = new HttpClient(getConnectionManager(params));
				xmlRpcCommonsTransportFactory.setHttpClient(httpClient);
				xmlRpcClient.setTransportFactory(xmlRpcCommonsTransportFactory);
			} else {
				XmlRpcWorkerFactory workerFactory = new SmXmlRpcClientWorkerFactory(xmlRpcClient);
				xmlRpcClient.setWorkerFactory(workerFactory);

				SmHttpTransportFactory transportFactory = new SmHttpTransportFactory(xmlRpcClient, params.getRetryCount());
				xmlRpcClient.setTransportFactory(transportFactory);
			}
			
			client = new DefaultXmlRpcClient();
			client.setNativeClient(xmlRpcClient);
		} catch (Exception e) {
			throw new XmlRpcException(e.getMessage(), e);
		}
		return client;
	}
	
	private MultiThreadedHttpConnectionManager getConnectionManager(ConfigParams params) {
	    HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
	    
	    connectionManagerParams.setConnectionTimeout(params.getConnectionTimeout());
	    connectionManagerParams.setMaxTotalConnections(params.getMaxTotalConnections());
	    connectionManagerParams.setStaleCheckingEnabled(params.isStaleCheckingEnabled());
	    connectionManagerParams.setSoTimeout(params.getSoTimeout());
	    
	    int connectionPerHost = params.getMaxConnectionsPerHost() == 0 ? params.getMaxTotalConnections() : params.getMaxConnectionsPerHost();
	    connectionManagerParams.setMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION, connectionPerHost);

	    MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	    connectionManager.setParams(connectionManagerParams);
	    
	    IdleConnectionTimeoutThread thread = new IdleConnectionTimeoutThread();
	    thread.setConnectionTimeout(params.getIdleConnctionTimeout());
	    thread.setTimeoutInterval(params.getIdleConnectionTimeoutInterval());
	    thread.addConnectionManager(connectionManager);
	    thread.start();
	    
	    return connectionManager;
	}

	private XmlRpcClientConfig createConfig(ConfigParams params) throws MalformedURLException {
		URL acipEndpointUrl = new URL(params.getUrl());
		XmlRpcClientConfigImpl xmlRpcConfig = new XmlRpcClientConfigImpl();
		xmlRpcConfig.setServerURL(acipEndpointUrl);

		xmlRpcConfig.setBasicUserName(params.getUsername());
		xmlRpcConfig.setBasicPassword(params.getPassword());
		xmlRpcConfig.setUserAgent(params.getUserAgent());
		xmlRpcConfig.setEnabledForExtensions(true);
		return xmlRpcConfig;
	}

}

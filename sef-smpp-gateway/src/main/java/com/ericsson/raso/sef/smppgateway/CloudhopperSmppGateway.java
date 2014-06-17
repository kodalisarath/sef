package com.ericsson.raso.sef.smppgateway;

import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.smpp.SmppServerConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;


public class CloudhopperSmppGateway implements SmppGateway {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private IConfig config;
	
	private Collection<com.cloudhopper.smpp.SmppServer> servers = new LinkedList<com.cloudhopper.smpp.SmppServer>();
	
	public CloudhopperSmppGateway(IConfig config) {
		this.config = config;
	}
	
	public CloudhopperSmppGateway() {
		//this.config = config;
	}
	
	public void createSmppGateway( String endpoint, SmppGatewayCallback callback) {
		try {
		//	SmppServerEndpoint serverConfig = config.smppServerEndpoint(endpointId);
			SmppServerEndpoint serverConfig = new SmppServerEndpoint();
			
			IConfig config = SefCoreServiceResolver.getConfigService();
			
			serverConfig.setPassword(config.getValue("Smsc_Gateway_Props", "serverPassword"));
			
			SmppServerConfiguration configuration = prepareConfiguration(endpoint, serverConfig);
			
			DefaultSmppServerHandler handler = new DefaultSmppServerHandler(callback);
			handler.setSystemId(configuration.getSystemId());
			handler.setPassword(serverConfig.getPassword());
			
			com.cloudhopper.smpp.SmppServer smppServer = new DefaultSmppServer(configuration, handler);
			smppServer.start();
			log.info("Smpp Server estabilized with Endpoint ID: " + endpoint);
			//System.out.println("Smpp Server estabilized with Endpoint ID: " + endpointId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public void destroy() {
		for (com.cloudhopper.smpp.SmppServer smppServer : servers) {
			smppServer.destroy();
		}
	}

	private SmppServerConfiguration prepareConfiguration(String endpoint, SmppServerEndpoint serverConfig) {
		SmppServerConfiguration configuration = new SmppServerConfiguration();
		String systemId = null;
		int port=0,bindTimeOut=0,maxConnectionSize =0,defaultWindowSize=0,waitTimeOut=0;
		IConfig config = SefCoreServiceResolver.getConfigService();
	
		maxConnectionSize = Integer.parseInt(config.getValue("Smsc_Gateway_Props", "maxConnectionSize"));
		defaultWindowSize =Integer.parseInt(config.getValue("Smsc_Gateway_Props", "defaultWindowSize"));
		bindTimeOut =Integer.parseInt(config.getValue("Smsc_Gateway_Props", "bindTimeOut"));
		waitTimeOut = Integer.parseInt(config.getValue("Smsc_Gateway_Props", "waitTimeOut"));
		port = Integer.parseInt(config.getValue("Smsc_Gateway_Props", "serverPort"));
		systemId = config.getValue("Smsc_Gateway_Props", "serverSystemId");
		//String destIp = config.getValue("Smsc_Props", "targetHost");
		
		configuration.setName(endpoint);
		configuration.setPort(port);
		configuration.setBindTimeout(bindTimeOut);
		configuration.setMaxConnectionSize(maxConnectionSize);
		configuration.setSystemId(systemId);
		configuration.setDefaultWindowSize(defaultWindowSize);
		configuration.setDefaultWindowWaitTimeout(waitTimeOut);
		
		return configuration;
	}
}

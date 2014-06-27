/*package com.ericsson.raso.sef.ne.core.smpp;

import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.smpp.SmppServerConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.ne.core.endpoint.SmppServerEndpoint;
import com.ericsson.raso.sef.ne.core.smpp.internal.DefaultSmppServerHandler;


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
	
	public void createSmppGateway(String endpointId, SmppGatewayCallback callback) {
		try {
		//	SmppServerEndpoint serverConfig = config.smppServerEndpoint(endpointId);
			SmppServerEndpoint serverConfig = new SmppServerEndpoint();
			
			SmppServerConfiguration configuration = prepareConfiguration(endpointId, serverConfig);
			
			DefaultSmppServerHandler handler = new DefaultSmppServerHandler(callback);
			handler.setSystemId(configuration.getSystemId());
			handler.setPassword(serverConfig.getPassword());
			
			com.cloudhopper.smpp.SmppServer smppServer = new DefaultSmppServer(configuration, handler);
			smppServer.start();
			log.info("Smpp Client estabilized with Endpoint ID: " + endpointId);
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

	private SmppServerConfiguration prepareConfiguration(String endpointId, SmppServerEndpoint serverConfig) {
		SmppServerConfiguration configuration = new SmppServerConfiguration();
		configuration.setName(endpointId);
		configuration.setPort(2775);
		configuration.setBindTimeout(200);
		configuration.setMaxConnectionSize(100);
		configuration.setSystemId(serverConfig.getSystemId());
		configuration.setDefaultWindowSize(serverConfig.getDefaultWindowSize());
		configuration.setDefaultWindowWaitTimeout(200);
		return configuration;
	}
}
*/
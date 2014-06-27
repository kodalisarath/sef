package com.ericsson.raso.sef.ne.core.smpp.internal;

import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.ne.core.smpp.SmppClient;
import com.ericsson.raso.sef.ne.core.smpp.SmppClientFactory;

public class JSmppClientFactory implements SmppClientFactory {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private IConfig config;
	
	private Collection<JSmppClient> clients = new LinkedList<JSmppClient>();
	
	public JSmppClientFactory(IConfig config) {
		this.config = config;
	}
	
	public JSmppClientFactory() {
		
	}
	
	
	public synchronized SmppClient create(String endpointId) {
		try {
		//	com.ericsson.raso.sef.ne.core.smpp.config.SmppClient clientConfig  = config.smppClientEndpoint(endpointId);

            //JSmppClient smppClient = new JSmppClient(clientConfig, clientConfig.getSourceAddress(), clientConfig.getDestinationAddress());
            JSmppClient smppClient = new JSmppClient();

            smppClient.start();
			log.debug("Smpp Client estabilized with Endpoint ID: " + endpointId);
			//System.out.println("Smpp Client estabilized with Endpoint ID: " + endpointId);
			clients.add(smppClient);
			return smppClient;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public void destroy() {
		Collection<JSmppClient> clients = new LinkedList<JSmppClient>();
		for (JSmppClient smppClient : clients) {
			smppClient.destroy();
		}
	}
}

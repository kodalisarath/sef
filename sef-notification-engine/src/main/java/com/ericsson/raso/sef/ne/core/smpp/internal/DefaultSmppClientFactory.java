package com.ericsson.raso.sef.ne.core.smpp.internal;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.config.Property;
import com.ericsson.raso.sef.core.config.Section;
import com.ericsson.raso.sef.ne.core.config.SourceSmppAddress;
import com.ericsson.raso.sef.ne.core.config.TargetSmppAddress;
import com.ericsson.raso.sef.ne.core.endpoint.SmppClientEndpoint;
import com.ericsson.raso.sef.ne.core.smpp.SmppClient;
import com.ericsson.raso.sef.ne.core.smpp.SmppClientFactory;

public class DefaultSmppClientFactory implements SmppClientFactory {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private IConfig config;
	
	private Collection<CloudhopperSmppClient> clients = new LinkedList<CloudhopperSmppClient>();
	
	public DefaultSmppClientFactory(IConfig config) {
		this.config = config;
	}
	public DefaultSmppClientFactory() {
		
	}
	
	public synchronized SmppClient create(String endpointId) {
		try {
			SmppClientEndpoint clientConfig  = new SmppClientEndpoint();

			IConfig config = SefCoreServiceResolver.getConfigService();
			//Section section = config.getSection("Global_smscClients");
			//List<Property> propertyList = SefCoreServiceResolver.getConfigService().getProperties(section);

			String userId = config.getValue("Smsc_Props", "userId");
			log.debug("user ID is "+userId);
			String password =config.getValue("Smsc_Props", "password");
			String srcIp =config.getValue("Smsc_Props", "sourceHost");
			String destIp=config.getValue("Smsc_Props", "targetHost");
			String port=config.getValue("Smsc_Props", "targetPort");
			
			int requestExpiryTimeout=Integer.parseInt(config.getValue("Smsc_Props", "requestExpiryTimeout"));
			int bindTimeOut = Integer.parseInt(config.getValue("Smsc_Props", "bindTimeOut"));
			int connectTimeOut = Integer.parseInt(config.getValue("Smsc_Props", "connectTimeOut"));
			
			//config.smppClientEndpoint(endpointId);
			SmppSessionConfiguration configuration = new SmppSessionConfiguration();
			configuration.setName(endpointId);
			configuration.setHost(destIp);
			configuration.setPort(Integer.parseInt(port));
			configuration.setSystemId(userId);
			configuration.setPassword(password);
			configuration.setSystemType(clientConfig.getSystemType());
			configuration.setWindowSize(5);
			configuration.setRequestExpiryTimeout(requestExpiryTimeout);
			configuration.setWindowMonitorInterval(clientConfig.getWindowMonitorInterval());
			configuration.setCountersEnabled(clientConfig.isCountersEnabled());
			configuration.setConnectTimeout(connectTimeOut);
			configuration.setBindTimeout(bindTimeOut);
			configuration.setType(SmppBindType.TRANSMITTER);
			
			SourceSmppAddress sourceSmppAddress = new SourceSmppAddress((byte) 1, (byte) 1, srcIp);
			TargetSmppAddress targetSmppAddress = new TargetSmppAddress((byte) 1, (byte) 1, destIp);
			
			CloudhopperSmppClient smppClient = new CloudhopperSmppClient(configuration, sourceSmppAddress, targetSmppAddress);
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
		Collection<CloudhopperSmppClient> clients = new LinkedList<CloudhopperSmppClient>();
		for (CloudhopperSmppClient smppClient : clients) {
			smppClient.destroy();
		}
	}
}

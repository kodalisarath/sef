package com.ericsson.raso.sef.client.air.internal;

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.AirClient;
import com.ericsson.raso.sef.client.air.request.AbstractAirRequest;
import com.ericsson.raso.sef.client.air.response.AbstractAirResponse;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.config.Section;
import com.ericsson.raso.sef.plugin.xmlrpc.ConfigParams;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcClient;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcClientFactory;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;
import com.ericsson.raso.sef.plugin.xmlrpc.internal.DefaultXmlRpcClientFactory;

public class AirClientImpl implements AirClient {
	
	private String originNodeType;
	private String originHostName;
	private String defaultSite;

	private Map<String, XmlRpcClient> clientMap = new HashMap<String, XmlRpcClient>();
	
	Logger logger = LoggerFactory.getLogger(AirClientImpl.class);
	
	public AirClientImpl() throws XmlRpcException {
		logger.debug("AIR Endpoint initialization starts!!!");		
		init();
	}

	private void init() throws XmlRpcException {
		XmlRpcClientFactory factory = new DefaultXmlRpcClientFactory();
		//assuming max 10 end points can be configured 
		for(int i=1; i<11; i++) {	
			String sectionName = "cs-air"+i;
			IConfig config = SefCoreServiceResolver.getConfigService();
			Section csAir = config.getSection(sectionName);
			if(csAir == null)
				continue;
			
			originHostName = SefCoreServiceResolver.getConfigService().getValue(sectionName, "originHostName");
			originNodeType = SefCoreServiceResolver.getConfigService().getValue(sectionName, "originNodeType");
			defaultSite = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultSite");
			
			logger.debug("originHostName: " + originHostName);
			logger.debug("originNodeType: " + originNodeType);
			logger.debug("defaultSite: " + defaultSite);
			
			ConfigParams configParams = createXmlRpcConfig(config, sectionName);
			XmlRpcClient client = factory.create(configParams);
			String siteId = SefCoreServiceResolver.getConfigService().getValue(sectionName, "siteId");
			clientMap.put(siteId, client);
			
		}
		logger.debug("AIR Endpoint initialized with " + clientMap.size() + "clients");
	}

	@Override
	public <R extends AbstractAirRequest, S extends AbstractAirResponse> void execute(R request, S response)
			throws XmlRpcException {
		if(request.getOriginTransactionId() == null) {
			request.setOriginTransactionId(String.valueOf(createTransactionId()));
		}
		
		String nai = CsAirContext.getProperty("subscriberNumberNAI");
		if(nai != null) {
			request.setSubscriberNumberNAI(Integer.valueOf(nai));
		}
		
		request.setOriginTimeStamp(new Date());
		request.setOriginHostName(originHostName);
		request.setOriginNodeType(originNodeType);

		try {
			AirEdrLogger.requestIn(request.getMethodName(), request.getParams());
			logger.debug("Fetching client for siteId: " +  request.getSiteId());
			XmlRpcClient xmlRpcClient = getXmlRpcClient(request.getSubscriberNumber(), request.getSiteId());
			xmlRpcClient.execute(request, response);
			AirEdrLogger.responseOut(request.getMethodName(), response.getResult());
		} catch (XmlRpcException e) {
			AirEdrLogger.responseError(request.getMethodName(), e);
			if(e.code == 0) {
				throw new XmlRpcException(SmException.globalError.getCode(), SmException.globalError.getMessage(), e.linkedException);
			} else if (e.linkedException instanceof SocketTimeoutException){
				throw new XmlRpcException(SmException.socketError.getCode(), SmException.socketError.getMessage(), e.linkedException);
			}
			else {
				throw e;
			}
		} catch (SmException e) {
			throw new XmlRpcException(e.getStatusCode().getCode(), e.getStatusCode().getMessage(), e);
		}
		
		if(response.isResponseAvailable()) {
			int responseCode = response.getResponseCode();
			if(responseCode != 0) {
				throw new XmlRpcException(responseCode, responseCode + " CS-AIR for msisdn: " + request.getSubscriberNumber());
			}
		}
	}
	
	private static long createTransactionId() {
		return 9999 + (int)(Math.random() * ((999999999 - 9999) + 1));
	}
	
	private XmlRpcClient getXmlRpcClient(String msisdn, String site) throws SmException {
		if(site!=null) {
			return clientMap.get(site);
		} 
		logger.debug("Probably no siteId specificied for the MSISDN, returning default site "+ defaultSite + " client" + clientMap.get(defaultSite));
		return clientMap.get(defaultSite);
	}
	
	private ConfigParams createXmlRpcConfig(IConfig config, String sectionName) {
		
		ConfigParams params = new ConfigParams();
		
		
		String timeout = config.getValue(sectionName, "connectionTimeout");
		int connectionTimeout;
		if (timeout !=null) {
			connectionTimeout = Integer.parseInt(timeout);
		} else {
			//default to 30000
			connectionTimeout = 30000;
		}
		params.setConnectionTimeout(connectionTimeout);
		
		String idleTimeout = config.getValue(sectionName, "idleConnctionTimeout");
		int idleConnectionTimeout;
		if(idleTimeout != null) {
			idleConnectionTimeout = Integer.parseInt(idleTimeout);
		} else {
			//default to 360000
			
			idleConnectionTimeout = 360000;
		}
		params.setIdleConnctionTimeout(idleConnectionTimeout);
		
		String idleTimeoutInterval = config.getValue(sectionName, "idleConnectionTimeoutInterval");
		int idleConnectionTimeoutInterval;
		if(idleTimeout != null) {
			idleConnectionTimeoutInterval = Integer.parseInt(idleTimeoutInterval);
		} else {
			//default to 60000
			idleConnectionTimeoutInterval = 60000;
		}
		params.setIdleConnectionTimeoutInterval(idleConnectionTimeoutInterval);
		
		String connectionsPerHost = config.getValue(sectionName, "maxConnectionsPerHost");
		int maxConnectionsPerHost;
		if(connectionsPerHost != null) {
			maxConnectionsPerHost = Integer.parseInt(connectionsPerHost);
		} else {
			maxConnectionsPerHost = 40;
		}
		params.setMaxConnectionsPerHost(maxConnectionsPerHost);

		String maxConnections = config.getValue(sectionName, "maxConnections");
		int maxTotalConnections;
		if(maxConnections != null) {
			maxTotalConnections = Integer.parseInt(maxConnections);
		} else {
			maxTotalConnections = 40;
		}
		params.setMaxTotalConnections(maxTotalConnections);
		
		String password = config.getValue(sectionName, "password");
		params.setPassword(password);
		
		String retries = config.getValue(sectionName, "retryCount");
		int retryCount;
		if (retries!=null) {
			retryCount = Integer.parseInt(retries);
		} else {
			retryCount = 3;
		}
		params.setRetryCount(retryCount);

		String staleCheck = config.getValue(sectionName, "staleCheckingEnabled");
		boolean staleCheckEnabled = false;
		
		if(staleCheck.equalsIgnoreCase("true")) staleCheckEnabled = true;
		
		params.setStaleCheckingEnabled(staleCheckEnabled);
		
		String useApache = config.getValue(sectionName, "useApacheHttpClient");
		boolean useApacheHttpClient = false;
		
		if(useApache.equalsIgnoreCase("true")) useApacheHttpClient = true;
		
		params.setUseApacheHttpClient(useApacheHttpClient);

		String userAgent = config.getValue(sectionName, "userAgent");
		params.setUserAgent(userAgent);

		String userName = config.getValue(sectionName, "username");
		params.setUsername(userName);
		
		String sotimeout = config.getValue(sectionName, "soTimeout");
		int soTimeout;
		
		if(sotimeout != null) {
			soTimeout = Integer.parseInt(sotimeout);
		} else {
			soTimeout = 120000;
		}
		
		params.setSoTimeout(soTimeout);
		
		String address = SefCoreServiceResolver.getConfigService().getValue(sectionName, "address");
		if(address == null)  throw new RuntimeException("Property missing in config.xml, Section: " + sectionName + ", Property: 'address'");
		params.setUrl(address);
		
		return params;
	}
	
}

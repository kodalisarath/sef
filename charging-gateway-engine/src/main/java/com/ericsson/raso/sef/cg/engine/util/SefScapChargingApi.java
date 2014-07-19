package com.ericsson.raso.sef.cg.engine.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.DCCStack;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.DiameterConfig;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.Diameter;
import com.ericsson.raso.sef.core.cg.diameter.DiameterStackBuilder;
import com.ericsson.raso.sef.core.cg.diameter.DiameterStackBuilder.Stack;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.lb.LoadBalancerPool;
import com.ericsson.raso.sef.core.lb.Member;


public class SefScapChargingApi implements ScapChargingApi {

	private DCCStack scapStack;
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	public static String Service_Context_Id = "SCAP_V.2.0@ericsson.com";
	//private String scapEndpointId;	
	private DiameterLoadBalancerPool loadBalancerPool;
	
	/*public SmScapChargingApi(String scapEndpointId) {
		this.scapEndpointId = scapEndpointId;
	}*/

	public Ccr createScapCcr(String sessionId, String hostId) {
		Ccr ccrMessage = new Ccr(sessionId, scapStack.getDiameterStack(), Service_Context_Id);
		Member route = loadBalancerPool.getMemberById(hostId);
		ccrMessage.setDestinationRealm(route.getRealm());
		ccrMessage.setDestinationHost(route.getHostId());
		ccrMessage.setOriginHost("il.smart.com.ph");
		return ccrMessage;
	}
	
	public Ccr createDummyCcr() {
		return new Ccr("dummy", scapStack.getDiameterStack(), Service_Context_Id);
	}
	
	public void start() {
		log.debug("Starting DCC STACK");
		IConfig config = SefCoreServiceResolver.getConfigService();	
		DiameterStackBuilder builder = new DiameterStackBuilder(Stack.SCAPV2);
		
		String fqdn = System.getenv("SCAPFQDN");
		
		if (fqdn.isEmpty() || fqdn == null) {
			fqdn = SefCoreUtil.getServerIP(config.getValue("scapClient", Constants.ETHINTERFACE));
		}
		log.debug("FQDN: " + fqdn);
		
		log.debug("Value getting picked from properties file: "+config.getValue("scapClient",Constants.PRODUCTID));
		builder.ownProductId(config.getValue("scapClient",Constants.PRODUCTID));
		builder.ownRealm(config.getValue("scapClient",Constants.REALM));
		builder.fqdn(fqdn.trim());
		builder.tcpPort(Integer.valueOf(config.getValue("scapClient",Constants.OWNTCPPORT)));
		//builder.ownIpAddress(SmCoreUtil.getServerIP(config.getValue("scapClient",Constants.ETHINTERFACE)));
		String ownIpAddress =  SefCoreUtil.getServerIP(config.getValue("scapClient","ethInterface"));
		if(ownIpAddress == null)ownIpAddress ="0.0.0.0";
		builder.ownIpAddress(ownIpAddress);
		
		log.debug("ownIpAddress  "+ownIpAddress);
		List<Member> routes = StaticRoutes();
		for (Member staticRoute : routes) {
			log.debug("staticRoute.getAddress()  "+staticRoute.getAddress());
			builder.addStaticRoute(staticRoute.getRealm(), staticRoute.getAddress());
		}
		scapStack = builder.build();
		scapStack.getDiameterConfig().setValue(DiameterConfig.NUMBER_OF_THREADS_THAT_HANDLES_RECEIVED_REQUESTS, config.getValue("scapClient",Constants.POOLSIZE));
		scapStack.getDiameterConfig().setValue(DiameterConfig.EVENT_QUEUE_SIZE , config.getValue("scapClient",Constants.EVENTQUEUESIZE));
		scapStack.getDiameterConfig().setValue(DiameterConfig.SEND_QUEUE_SIZE, config.getValue("scapClient",Constants.SENDQUEUESIZE));
		scapStack.getDiameterConfig().setValue(DiameterConfig.SEND_MESSAGE_LIMIT , config.getValue("scapClient",Constants.SENDMESSAGELIMIT));
		
		scapStack.getDiameterConfig().addSupportedVendor(10415);

		try {
			scapStack.start();
			log.debug("DCC STACK STARTED");
		} catch (Exception e) {
			log.error("Error while starting the scap client", e);
			throw new RuntimeException(e);
		}
		loadBalancerPool = new DiameterLoadBalancerPool(routes);
		scapStack.getDiameterStack().addPeerConnectionListener(loadBalancerPool);
		log.debug("loadBalancerPool added ..ALl Done  "+loadBalancerPool);
	/*	String site = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultSite");
		log.debug("STARTED...DEBUG1");
		getLoadBalancerPool().getMemberBySite(site);
		log.debug("ENDED...DEBUG1");*/
	}

	public LoadBalancerPool getLoadBalancerPool() {
		log.debug("The loadbalceerPool is "+loadBalancerPool);
		return loadBalancerPool;
	}
	
	public void stop() {
		scapStack.stop();
	}
	
	private List<Member> StaticRoutes(){
		IConfig config = SefCoreServiceResolver.getConfigService();	
		List<Member> staticRoutes = new ArrayList<Member>();
		Integer staticRoute_intancesCount = Integer.valueOf(config.getValue("scapClient","StaticRoute_Instances"));
		for(int i =1; i<= staticRoute_intancesCount;i++){
			Member staticRoute = new Member();
			staticRoute.setAddress(config.getValue("scapClient","address"+i));
			staticRoute.setHostId(config.getValue("scapClient","hostId"+i));
			staticRoute.setRealm(config.getValue("scapClient","realm"+i));
			staticRoute.setSiteId(config.getValue("scapClient","siteId"+i));
			staticRoutes.add(staticRoute);
		}
		return staticRoutes;
	}

	/*public Diameter getDiameterConfig() {
		// TODO Auto-generated method stub
		return null;
	}*/
}
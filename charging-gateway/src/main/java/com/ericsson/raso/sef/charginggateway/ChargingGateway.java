package com.ericsson.raso.sef.charginggateway;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import com.ericsson.pps.diameter.dccapi.DCCStack;
import com.ericsson.pps.diameter.rfcapi.base.DiameterConfig;
import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.ApplicationId;
import com.ericsson.raso.sef.charginggateway.diameter.Diameter;
import com.ericsson.raso.sef.charginggateway.diameter.DiameterStackBuilder;
import com.ericsson.raso.sef.charginggateway.diameter.Protocol;
import com.ericsson.raso.sef.charginggateway.diameter.ProtocolType;
import com.ericsson.raso.sef.charginggateway.diameter.DiameterStackBuilder.Stack;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.lb.Member;

public class ChargingGateway implements SmartLifecycle {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	//private String diameterEndpoint;
	private CgPeerConnectionListener connectionListener;
	private CgRequestListener requestListener;

	private DCCStack dccStack;
	private Diameter diameter;

	public ChargingGateway( CgPeerConnectionListener connectionListener,
			CgRequestListener requestListener) {
		//this.diameterEndpoint = diameterEndpoint;
		this.connectionListener = connectionListener;
		this.requestListener = requestListener;
		diameter = new Diameter();
	}

	@Override
	public void start() {
	
		//To Do --  Diameter class is updated.
		IConfig config = SefCoreServiceResolver.getConfigService();
		diameter.setRealm(config.getValue("diameterEndpoint","realm"));
		diameter.setAcceptUnknownPeers(false);
		diameter.setAccountId(Long.parseLong(config.getValue("diameterEndpoint", "accountId")));
		diameter.setAuthId(Long.parseLong(config.getValue("diameterEndpoint", "authId")));
		//diameter.setComponentName(componentName);
		//diameter.setDuplicateRequestDetectionActive(duplicateRequestDetectionActive);
		diameter.setEndpointId(config.getValue("diameterEndpoint","endpointId"));
		diameter.setEthInterface(config.getValue("diameterEndpoint","ethInterface"));
		diameter.setEventQueueSize(Integer.parseInt(config.getValue("diameterEndpoint","eventQueueSize")));
		//diameter.setFirmwareRevision(config.getValue("diameterEndpoint","endpointId"));
		diameter.setFqdn(config.getValue("diameterEndpoint","fqdn"));
		//diameter.setIpAddress(config.getValue("diameterEndpoint","endpointId"));
		diameter.setMessageTimeout(Long.parseLong(config.getValue("diameterEndpoint","messageTimeout")));
		diameter.setVendorId(Long.parseLong(config.getValue("diameterEndpoint","vendorId")));
/*		
		List<Member> routes = getStaticRoutes();
		
		for (Member staticRoute : routes) {
			builder.addStaticRoute(staticRoute.getRealm(), staticRoute.getAddress());
		}
		
		diameter.setStaticRoutes();*/
		
		//diameter.setStackUri(config.getValue("diameterEndpoint","endpointId"));
//		/diameter.setStackParams(stackParams);
		diameter.setSendQueueSize(Integer.parseInt(config.getValue("diameterEndpoint","sendQueueSize")));
		diameter.setSendMessageLimit(Integer.parseInt(config.getValue("diameterEndpoint","sendMessageLimit")));
		diameter.setProtocolType(ProtocolType.server);
		//diameter.setProtocol(Protocol.basediameter);
		diameter.setProductId(config.getValue("diameterEndpoint","productId"));
		diameter.setPoolSize(Integer.parseInt(config.getValue("diameterEndpoint","poolSize")));
		diameter.setOwnTcpPort(Integer.parseInt(config.getValue("diameterEndpoint","ownTcpPort")));
		
		
		ApplicationId applicationId = new ApplicationId(diameter.getVendorId(), diameter.getAccountId(),
				diameter.getAuthId());
		log.debug("ChargingGateway Application is is "+applicationId);
		DiameterStackBuilder builder = new DiameterStackBuilder(Stack.DCC);
		builder.
			application(applicationId).
			fqdn(diameter.getFqdn()).
			messageTimeout(diameter.getMessageTimeout()).
			ownProductId(diameter.getProductId()).
			ownRealm(diameter.getRealm()).
			ownStackUri(diameter.getStackUri()).
			peerConnectionListener(connectionListener).
			requestListener(requestListener, applicationId)
			.ownIpAddress(config.getValue("diameterEndpoint","ownIpAddress"));
	
		dccStack = builder.build();
		dccStack.getDiameterConfig().setValue(DiameterConfig.ACCEPT_UNKNOWN_PEERS, true);
		dccStack.getDiameterConfig().setValue(DiameterConfig.NUMBER_OF_THREADS_THAT_HANDLES_RECEIVED_REQUESTS, diameter.getPoolSize());
		
		dccStack.getDiameterConfig().setValue(DiameterConfig.EVENT_QUEUE_SIZE , diameter.getEventQueueSize());
		dccStack.getDiameterConfig().setValue(DiameterConfig.SEND_QUEUE_SIZE, diameter.getSendQueueSize());
		dccStack.getDiameterConfig().setValue(DiameterConfig.SEND_MESSAGE_LIMIT , diameter.getSendMessageLimit());
		
		requestListener.setTimeout(diameter.getMessageTimeout());
		try {
			log.info("Starting Diameter server for uri: " + diameter.getStackUri());
			dccStack.start();
			log.info("Diameter server is started for uri: " + diameter.getStackUri());
		} catch (Exception e) {
			log.error("Exception captured ChargingGateway@Process is ",e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop() {
		log.info("Stopping Diameter server for uri: " + diameter.getStackUri());
		dccStack.stop();
		log.info("Diameter server is stopped for uri: " + diameter.getStackUri());
	}

	@Override
	public boolean isRunning() {
		if(dccStack == null) {
			return false;
		}
		
		log.debug("IsRunning is ... "+dccStack.getDiameterStack().isStarted());
		return dccStack.getDiameterStack().isStarted();
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		log.info("Stopping Diameter server for uri: " + diameter.getStackUri());
		dccStack.stop();
		log.info("Diameter server is stopped for uri: " + diameter.getStackUri());
	}

	private List<Member> getStaticRoutes(){
		IConfig config = SefCoreServiceResolver.getConfigService();	
		List<Member> staticRoutes = new ArrayList<Member>();
		Integer staticRoute_intancesCount = Integer.valueOf(config.getValue("diameterEndpoint","StaticRoute_Instances"));
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
}

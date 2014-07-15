package com.ericsson.raso.sef.cg.engine.processor;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.IpcCluster;
import com.ericsson.raso.sef.cg.engine.SmartChargingSession;
import com.ericsson.raso.sef.core.FetchRequestContextTask;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.StaticRoute;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.ericsson.raso.sef.core.lb.LoadBalancerPool;
import com.ericsson.raso.sef.core.lb.Member;
import com.google.gson.Gson;

public class RouteProcessor implements Processor {

	public static final String SITEID = "siteId";
	private static Logger log = LoggerFactory.getLogger(RouteProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();
		ChargingSession session = SefCoreServiceResolver.getChargingSessionService().get(request.getSessionId());
		SmartChargingSession smartSession = null;
		
		if (session == null) {
			session = new ChargingSession();
			session.setSessionId(request.getSessionId());
			session.setCreationTime(new Date());
			session.setExpiryTime(new Date(System.currentTimeMillis() + Long.parseLong(SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "chargingSessionExpiry"))));
			smartSession = new SmartChargingSession();
			smartSession.setHostId(request.getHostId());
			smartSession.setMessageId(request.getMessageId());
			smartSession.setMsisdn(request.getMsisdn());
			smartSession.setOperation(request.getOperation());
			session.setSessionInfo(this.getSessionInfo(smartSession));
		} else {
			smartSession = this.getSmartSession(session.getSessionInfo());
		}

		switch (request.getOperation().getType()) {
		case TRANSACTION_END:
			request.setHostId(smartSession.getHostId());
			break;
		default:
			//String site = new FetchRequestContextTask().execute().get(SITEID);
			String site = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultSite");
			
			LoadBalancerPool pool = CgEngineContext.getChargingApi().getLoadBalancerPool();
			log.debug("STARTED...DEBUG2");
			Member route = pool.getMemberBySite(site);
			log.debug("ENDED...DEBUG2");
			request.setHostId(route.getHostId());
			smartSession.setHostId(route.getHostId());
			session.setSessionInfo(this.getSessionInfo(smartSession));
			SefCoreServiceResolver.getChargingSessionService().put(session);
			
			break;
		}
	}
	
	private SmartChargingSession getSmartSession(String sessionInfo) {
		Gson gson = new Gson();
		return gson.fromJson(sessionInfo, SmartChargingSession.class);		
	}
	
	private String getSessionInfo(SmartChargingSession session) {
		Gson gson = new Gson();
		return gson.toJson(session);
	}
}

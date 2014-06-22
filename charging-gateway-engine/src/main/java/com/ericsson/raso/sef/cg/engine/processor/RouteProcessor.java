package com.ericsson.raso.sef.cg.engine.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ChargingSession;
import com.ericsson.raso.sef.cg.engine.IpcCluster;
import com.ericsson.raso.sef.charginggateway.diameter.StaticRoute;
import com.ericsson.raso.sef.core.FetchRequestContextTask;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.lb.LoadBalancerPool;
import com.ericsson.raso.sef.core.lb.Member;

public class RouteProcessor implements Processor {

	public static final String SITEID = "siteId";
	private static Logger log = LoggerFactory.getLogger(RouteProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();
	//	IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(request.getSessionId());
		
		switch (request.getOperation().getType()) {
		case TRANSACTION_END:
			request.setHostId(session.getHostId());
			break;
		default:
			//String site = new FetchRequestContextTask().execute().get(SITEID);
			String site = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultSite");
			
			LoadBalancerPool pool = CgEngineContext.getChargingApi().getLoadBalancerPool();
			log.debug("STARTED...DEBUG2");
			Member route = pool.getMemberBySite(site);
			log.debug("ENDED...DEBUG2");
			request.setHostId(route.getHostId());
			session.setHostId(route.getHostId());
			CgEngineContext.getIpcCluster().updateChargingSession(session.getSessionId(), session);
			
			break;
		}
	}
}

package com.ericsson.raso.sef.cg.engine.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.CgConstants;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.cg.model.Operation.Type;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.ericsson.raso.sef.core.lb.LoadBalancerPool;
import com.ericsson.raso.sef.core.lb.Member;

public class TransactionIdentityProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();

		if(log.isDebugEnabled())
		log.debug("Starting TransactionIdentityProcessor ChargingRequest is "
				+ request);
		// String sessionId = request.getSourceCcr().getSessionId();
		try {
			String sessionId = (String) exchange.getIn().getHeader(
					CgConstants.sessionId);
			request.setSessionId(sessionId);

			// exchange.getIn().setHeader(CgConstants.sessionId, sessionId);
			ChargingSession session = null;
			if (request.getOperation().getType() == Type.TRANSACATION_START
					|| request.getOperation().getType() == Type.NO_TRANSACTION) {

				if(log.isDebugEnabled())
				log.debug("Starting TransactionIdentityProcessor Inside the if loop operation is "
						+ request.getOperation().getType());

				session = new ChargingSession();
				session.setSessionId(request.getSessionId());
				// session.setCreationTime(Calendar.getInstance().getTimeInMillis());
				// session.setExpiryTime(System.currentTimeMillis() +
				// Long.parseLong(SefCoreServiceResolver.getConfigService().getValue("GLOBAL",
				// "chargingSessionExpiry")));
				// SmartChargingSession smartSession = new
				// SmartChargingSession();

				String site = SefCoreServiceResolver.getConfigService()
						.getValue("GLOBAL", "defaultSite");

				LoadBalancerPool pool = CgEngineContext.getChargingApi()
						.getLoadBalancerPool();
				if(log.isDebugEnabled())
				log.debug("STARTED...DEBUG2");
				Member route = pool.getMemberBySite(site);
				if(log.isDebugEnabled())
				log.debug("ENDED...DEBUG2");
				request.setHostId(route.getHostId());
				session.setHostId(route.getHostId());
				session.setMessageId(request.getMessageId());
				session.setMsisdn(request.getMsisdn());
				session.setOperation(request.getOperation());
				// session.setSessionInfo(this.getSessionInfo(smartSession));

				if(request.getOperation().getType() == Type.TRANSACATION_START)
				{
				SefCoreServiceResolver.getChargingSessionService().create(
						session);

				if(log.isDebugEnabled())
				log.debug("Session DB update successfull");
				}
				else
				{
					if(log.isDebugEnabled())
					log.debug("Session Not required to be stored in the database. Operation is "+request.getOperation());
				}

			} else if (request.getOperation().getType() == Type.INTERMEDIATE_TRANSACTION
					|| request.getOperation().getType() == Type.TRANSACTION_END) {
				if(log.isDebugEnabled())
				log.debug("Starting TransactionIdentityProcessor Inside the ELSE loop operation is "
						+ request.getOperation().getType());
				session = SefCoreServiceResolver.getChargingSessionService()
						.read(sessionId);
				if(log.isDebugEnabled())
				log.debug("Starting TransactionIdentityProcessor session is "
						+ session);

				// SmartChargingSession smartSession = null;
				if (session == null) {
					log.error("Invalid request with session ID: " + sessionId);
					throw new SmException(
							ResponseCode.DIAMETER_UNKNOWN_SESSION_ID);

				}
				request.setHostId(session.getHostId());
				/*
				 * else smartSession =
				 * this.getSmartSession(session.getSessionInfo());
				 * 
				 * if (smartSession == null ||
				 * smartSession.getOperation().getType() !=
				 * Type.TRANSACATION_START) {
				 * log.error("Invalid request with session ID: " + sessionId);
				 * throw new
				 * SmException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID); }
				 */
			}

			request.setChargingSession(session);
			if(log.isDebugEnabled())
			log.debug("Ending TransactionIdentityProcessor ChargingRequest is "
					+ request);

			exchange.getIn().setBody(request);
		} catch (Exception e) {

			log.error("Exception caught in TransactionIdentityProcessor", e);
			throw e;
		}
	}

	/*
	 * private SmartChargingSession getSmartSession(String sessionInfo) { Gson
	 * gson = new Gson(); return gson.fromJson(sessionInfo,
	 * SmartChargingSession.class); }
	 * 
	 * private String getSessionInfo(SmartChargingSession session) { Gson gson =
	 * new Gson(); return gson.toJson(session); }
	 */
}
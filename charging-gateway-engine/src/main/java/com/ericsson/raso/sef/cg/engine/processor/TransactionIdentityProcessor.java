package com.ericsson.raso.sef.cg.engine.processor;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.CgConstants;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.Operation.Type;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.SmartChargingSession;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.google.gson.Gson;

public class TransactionIdentityProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();
		String sessionId = request.getSourceCcr().getSessionId();
		request.setSessionId(sessionId);

		exchange.getIn().setHeader(CgConstants.sessionId, sessionId);

		

		if (request.getOperation().getType() == Type.TRANSACATION_START || request.getOperation().getType() == Type.NO_TRANSACTION) {
			ChargingSession session = new ChargingSession();
			session.setSessionId(request.getSessionId());
			session.setCreationTime(new Date());
			session.setExpiryTime(new Date(System.currentTimeMillis() + Long.parseLong(SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "chargingSessionExpiry"))));
			SmartChargingSession smartSession = new SmartChargingSession();
			smartSession.setHostId(request.getHostId());
			smartSession.setMessageId(request.getMessageId());
			smartSession.setMsisdn(request.getMsisdn());
			smartSession.setOperation(request.getOperation());
			session.setSessionInfo(this.getSessionInfo(smartSession));
			SefCoreServiceResolver.getChargingSessionService().put(session);
			
		} else if (request.getOperation().getType() == Type.INTERMEDIATE_TRANSACTION || request.getOperation().getType() == Type.TRANSACTION_END) {
			ChargingSession session = SefCoreServiceResolver.getChargingSessionService().get(sessionId);
			SmartChargingSession smartSession = null;
			if (session == null) {
				log.error("Invalid request with session ID: " + sessionId);
				throw new SmException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID);
				
			} else
				smartSession = this.getSmartSession(session.getSessionInfo());
			
			if (smartSession == null || smartSession.getOperation().getType() != Type.TRANSACATION_START) {
				log.error("Invalid request with session ID: " + sessionId);
				throw new SmException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID);
			}
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
	
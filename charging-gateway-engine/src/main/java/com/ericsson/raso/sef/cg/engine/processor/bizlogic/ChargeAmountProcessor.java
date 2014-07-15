package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.SmartChargingSession;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.google.gson.Gson;

public class ChargeAmountProcessor extends AbstractChargingProcessor {

	private final static Logger logger = LoggerFactory.getLogger(ChargeAmountProcessor.class);
	@Override
	protected Integer getRequestNumber() {
		return 0;
	}

	@Override
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		logger.debug("In ChargeAmountProcessor.preProcess, no biz logic for this");

	}

	@Override
	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		logger.debug(String.format("Enter ChargeAmountProcessor.postProcess request is %s, response is %s, cca is %s", 
				request, response, cca));
		ChargingSession session = SefCoreServiceResolver.getChargingSessionService().get(request.getSessionId());
		SmartChargingSession smartSession = null;
		if (session == null) {
			log.error("Invalid request with session ID: " + request.getSessionId());
			throw new AvpDataException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID.getMessage());

		}
		
		smartSession = this.getSmartSession(session.getSessionInfo());
		if (smartSession == null) {
			log.error("Invalid request with session ID: " + request.getSessionId());
			throw new AvpDataException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID.getMessage());
		}
		
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			smartSession.setTransactionStatus(TransactionStatus.PROCESSED);
		} else {
			smartSession.setTransactionStatus(TransactionStatus.FAILED);
		}
		session.setSessionInfo(this.getSessionInfo(smartSession));
		SefCoreServiceResolver.getChargingSessionService().put(session);
		logger.debug("End ChargeAmountProcessor.postProcess");
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

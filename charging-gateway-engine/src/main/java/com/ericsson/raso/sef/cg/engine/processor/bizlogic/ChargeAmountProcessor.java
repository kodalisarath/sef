package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.model.TransactionStatus;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public class ChargeAmountProcessor extends AbstractChargingProcessor {

	private final static Logger logger = LoggerFactory.getLogger(ChargeAmountProcessor.class);
	@Override
	protected Integer getRequestNumber() {
		return 0;
	}

	@Override
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		if(logger.isDebugEnabled())
		logger.debug("In ChargeAmountProcessor.preProcess, no biz logic for this");

	}

	@Override
	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		if(logger.isDebugEnabled())
		logger.debug(String.format("Enter ChargeAmountProcessor.postProcess request is %s, response is %s, cca is %s", 
				request, response, cca));
		//ChargingSession session = SefCoreServiceResolver.getChargingSessionService().get(request.getSessionId());
		ChargingSession session = request.getChargingSession();
		//SmartChargingSession smartSession = null;
		if (session == null) {
			log.error("Invalid request with session ID: " + request.getSessionId());
			throw new AvpDataException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID.getMessage());

		}
		
	/*	smartSession = this.getSmartSession(session.getSessionInfo());
		if (smartSession == null) {
			log.error("Invalid request with session ID: " + request.getSessionId());
			throw new AvpDataException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID.getMessage());
		}*/
		
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			session.setTransactionStatus(TransactionStatus.PROCESSED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		request.setChargingSession(session);
		//session.setSessionInfo(this.getSessionInfo(smartSession));
		//SefCoreServiceResolver.getChargingSessionService().put(session);
		if(logger.isDebugEnabled())
		logger.debug("End ChargeAmountProcessor.postProcess, Modified request Object is "+request);
 	}
	
/*	private SmartChargingSession getSmartSession(String sessionInfo) {
		Gson gson = new Gson();
		return gson.fromJson(sessionInfo, SmartChargingSession.class);		
	}
	
	private String getSessionInfo(SmartChargingSession session) {
		Gson gson = new Gson();
		return gson.toJson(session);
	}*/
}

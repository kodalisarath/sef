package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.avp.CCMoneyAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCServiceSpecificUnitsAvp;
import com.ericsson.pps.diameter.dccapi.avp.CurrencyCodeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceIdentifierAvp;
import com.ericsson.pps.diameter.dccapi.avp.UnitValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.UsedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.ValueDigitsAvp;
import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.util.SefCoreUtil;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.model.Operation.Type;
import com.ericsson.raso.sef.core.cg.model.TransactionStatus;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public class CancelAmountProcessor extends AbstractChargingProcessor {
	
	private final static Logger logger = LoggerFactory.getLogger(CancelAmountProcessor.class);
	@Override
	protected Integer getRequestNumber() {
		return 1;
	}

	@Override
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		if(logger.isDebugEnabled())
		logger.debug(String.format("Enter CancelAmountProcessor.preProcess, request is %s, scapCcr is %s", request, scapCcr));
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
		
		session = SefCoreUtil.convertFromGSONFormat(session);
		
		if(logger.isDebugEnabled())
			logger.debug(String.format("Inside CommitAmountProcessor.preProcess, session after  JSON conversion is %s", session));
	
		List<Avp> responseAvps = session.getResponseAvp(Type.TRANSACATION_START);

		if(logger.isDebugEnabled())
			logger.debug(String.format("Inside CancelAmountProcessor.preProcess, responseAvps for TRANSACATION_START is %s", responseAvps));
		
		UsedServiceUnitAvp usedServiceUnitAvp = getUsedServiceUnitAvp(responseAvps,scapCcr);
		scapCcr.addAvp(usedServiceUnitAvp);
		request.setChargingSession(session);
		if(logger.isDebugEnabled())
		logger.debug("End CancelAmountProcessor.preProcess, response avp is "+ responseAvps +" modified request is "+request);
	}

	@Override
	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		if(logger.isDebugEnabled())
		logger.debug(String.format("Enter CancelAmountProcessor.preProcess, request is %s, response is %s, cca is %s", 
				request, response, cca));

		//ChargingSession session = SefCoreServiceResolver.getChargingSessionService().read(request.getSessionId());
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
		
		//session.setSessionInfo(this.getSessionInfo(smartSession));
		//SefCoreServiceResolver.getChargingSessionService().put(session);
		request.setChargingSession(session);
		if(logger.isDebugEnabled())
		logger.debug("End CancelAmountProcessor.postProcess and modified request is "+request);
	}

	private UsedServiceUnitAvp getUsedServiceUnitAvp(List<Avp> avps,Ccr scapCcr) throws AvpDataException {

		if(logger.isDebugEnabled())
		logger.debug(String.format("Enter CancelAmountProcessor.preProcess, avps is %s, scapCcr is %s", avps, scapCcr));
		UsedServiceUnitAvp usedServiceUnitAvp = new UsedServiceUnitAvp();
		
		if(scapCcr.getAvp(ServiceIdentifierAvp.AVP_CODE).getAsInt() == 7002)
		{
			CCMoneyAvp moneyAvp = new CCMoneyAvp();
			moneyAvp.addSubAvp(new CurrencyCodeAvp(608));

			UnitValueAvp unitValueAvp = new UnitValueAvp();
			ValueDigitsAvp valueDigitsAvp = new ValueDigitsAvp(0);

			unitValueAvp.addSubAvp(valueDigitsAvp);

			unitValueAvp.addExponent(0);

			moneyAvp.addSubAvp(unitValueAvp);

			usedServiceUnitAvp.addSubAvp(moneyAvp);

		}
		else
		{
			usedServiceUnitAvp.addSubAvp(new CCServiceSpecificUnitsAvp(0));		
		}


		return usedServiceUnitAvp;
	}
	
	/*private SmartChargingSession getSmartSession(String sessionInfo) {
		Gson gson = new Gson();
		return gson.fromJson(sessionInfo, SmartChargingSession.class);		
	}
	
	private String getSessionInfo(SmartChargingSession session) {
		Gson gson = new Gson();
		return gson.toJson(session);
	}*/
}
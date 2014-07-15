package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.avp.CCMoneyAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCServiceSpecificUnitsAvp;
import com.ericsson.pps.diameter.dccapi.avp.CurrencyCodeAvp;
import com.ericsson.pps.diameter.dccapi.avp.GrantedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.MultipleServicesCreditControlAvp;
import com.ericsson.pps.diameter.dccapi.avp.UnitValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.ValueDigitsAvp;
import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.Operation.Type;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.SmartChargingSession;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.google.gson.Gson;

public class ReserveAmountProcessor extends AbstractChargingProcessor {

	private final static Logger logger = LoggerFactory.getLogger(ChargeAmountProcessor.class);
	
	protected Integer getRequestNumber() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		logger.debug(String.format("Enter ReserveAmountProcessor.preProcess, request is %s, scapCcr is %s", request, scapCcr));
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
		

		smartSession.addRequestAvp(Type.TRANSACATION_START, scapCcr.getDiameterMessage().getAvps());
		session.setSessionInfo(this.getSessionInfo(smartSession));
		SefCoreServiceResolver.getChargingSessionService().put(session);
		logger.debug("End ReserveAmountProcessor.preProcess");
	}

	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
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
		

		smartSession.addResponseAvp(Type.TRANSACATION_START, response.getAvpList());
	
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			response.getAvpList().add(createCreditControlAvp(cca, request));
			smartSession.setTransactionStatus(TransactionStatus.AUTHORIZED);
		} else {
			smartSession.setTransactionStatus(TransactionStatus.FAILED);
		}
		
		session.setSessionInfo(this.getSessionInfo(smartSession));
		SefCoreServiceResolver.getChargingSessionService().put(session);
		logger.debug("End ReserveAmountProcessor.postProcess");
	}

	private MultipleServicesCreditControlAvp createCreditControlAvp(Cca cca, ChargingRequest request)
			throws AvpDataException {
		logger.debug(String.format("Enter ReserveAmountProcessor.createCreditControlAvp, cca is %s, request is %s", cca, request));
		MultipleServicesCreditControlAvp ccAvp = new MultipleServicesCreditControlAvp();
		
		GrantedServiceUnitAvp gsUnitAvp = new GrantedServiceUnitAvp(cca.getAvp(GrantedServiceUnitAvp.AVP_CODE));
		if (gsUnitAvp.getSubAvp(CCMoneyAvp.AVP_CODE) != null) {
			ccAvp.addSubAvp(gsUnitAvp);
		} else if (gsUnitAvp.getSubAvp(CCServiceSpecificUnitsAvp.AVP_CODE) != null) {
			GrantedServiceUnitAvp gsUnitAvp2 = new GrantedServiceUnitAvp();
			CCServiceSpecificUnitsAvp ccSSUnitsAvp = new CCServiceSpecificUnitsAvp(gsUnitAvp.getSubAvp(CCServiceSpecificUnitsAvp.AVP_CODE));
			CCMoneyAvp moneyAvp = new CCMoneyAvp();
			moneyAvp.addSubAvp(new CurrencyCodeAvp(608));
			UnitValueAvp unitValueAvp = new UnitValueAvp();

			ValueDigitsAvp digitsAvp = new ValueDigitsAvp(ccSSUnitsAvp.getAsLong());
			unitValueAvp.addSubAvp(digitsAvp);
			moneyAvp.addSubAvp(unitValueAvp);
			gsUnitAvp2.addSubAvp(moneyAvp);
			ccAvp.addSubAvp(gsUnitAvp2);

		}
		logger.debug("End ReserveAmountProcessor.createCreditControlAvp");
		return ccAvp;
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

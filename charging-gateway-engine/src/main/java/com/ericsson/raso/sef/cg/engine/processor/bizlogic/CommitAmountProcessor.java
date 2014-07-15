package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.avp.CCMoneyAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCServiceSpecificUnitsAvp;
import com.ericsson.pps.diameter.dccapi.avp.GrantedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.MultipleServicesCreditControlAvp;
import com.ericsson.pps.diameter.dccapi.avp.RequestedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.UnitValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.UsedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.ValueDigitsAvp;
import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.Operation.Type;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.SmartChargingSession;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.google.gson.Gson;

public class CommitAmountProcessor extends AbstractChargingProcessor {

	private final static Logger logger = LoggerFactory.getLogger(CancelAmountProcessor.class);
	
	@Override
	protected Integer getRequestNumber() {
		return 1;
	}

	@Override
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		logger.debug(String.format("Enter CommitAmountProcessor.preProcess, request is %s, scapCcr is %s",  request, scapCcr));
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
		

		List<Avp> responseAvps = smartSession.getResponseAvp(Type.TRANSACATION_START);
		List<Avp> reqAvps = smartSession.getRequestAvp(Type.TRANSACATION_START);
		UsedServiceUnitAvp usedServiceUnitAvp = getUsedServiceUnitAvpre(responseAvps, reqAvps, scapCcr);
		scapCcr.addAvp(usedServiceUnitAvp);
		logger.debug(String.format("End CommitAmountProcessor.preProcess, response avp is %s", responseAvps));
	}

	@Override
	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		logger.debug(String.format("Enter CommitAmountProcessor.preProcess, request is %s, response is %s, cca is %s", request, response, cca));
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
		

		if (cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			smartSession.setTransactionStatus(TransactionStatus.PROCESSED);
		} else {
			smartSession.setTransactionStatus(TransactionStatus.FAILED);
		}
		session.setSessionInfo(this.getSessionInfo(smartSession));
		SefCoreServiceResolver.getChargingSessionService().put(session);
		logger.debug("End CommitAmountProcessor.postProcess");
	}

	private UsedServiceUnitAvp getUsedServiceUnitAvpre(List<Avp> resAvps, List<Avp> reqAvps, Ccr scapCcr)
			throws AvpDataException {
	
		logger.debug(String.format("Enter CommitAmountProcessor.getUsedServiceUnitAvpre, resAvps is %s, reqAvps is %s, scapCcr is %s", 
				resAvps, reqAvps, scapCcr));
		
		Ccr resCcr = CgEngineContext.getChargingApi().createDummyCcr();
		resCcr.addAvps(resAvps);

		Ccr reqCcr = CgEngineContext.getChargingApi().createDummyCcr();
		reqCcr.addAvps(reqAvps);

		UsedServiceUnitAvp usedServiceUnitAvp = new UsedServiceUnitAvp();

		Avp msccAvpReq = scapCcr.getAvp(MultipleServicesCreditControlAvp.AVP_CODE);
		MultipleServicesCreditControlAvp msccAvp = null;
		if (msccAvpReq != null) {
			long resultSSU = 0, preUsu = 0, preRsu = 0, preGsu = 0;

			msccAvp = new MultipleServicesCreditControlAvp(msccAvpReq);
			UsedServiceUnitAvp usuAvp = new UsedServiceUnitAvp(msccAvp.getSubAvp(UsedServiceUnitAvp.AVP_CODE));
			if (usuAvp != null) {
				if (usuAvp.getSubAvp(CCMoneyAvp.AVP_CODE) != null) {
					CCMoneyAvp ccMoneyAvp = new CCMoneyAvp(usuAvp.getSubAvp(CCMoneyAvp.AVP_CODE));
					UnitValueAvp unitValAvp = new UnitValueAvp(ccMoneyAvp.getSubAvp(UnitValueAvp.AVP_CODE));
					ValueDigitsAvp valueDigitsAvp = new ValueDigitsAvp(unitValAvp.getSubAvp(ValueDigitsAvp.AVP_CODE));
					preUsu = valueDigitsAvp.getAsLong();
				}
			}

			MultipleServicesCreditControlAvp msccAvprsu = new MultipleServicesCreditControlAvp(
					reqCcr.getAvp(MultipleServicesCreditControlAvp.AVP_CODE));
			RequestedServiceUnitAvp rsuAvp = new RequestedServiceUnitAvp(
					msccAvprsu.getSubAvp(RequestedServiceUnitAvp.AVP_CODE));
			if (rsuAvp != null) {
				if (rsuAvp.getSubAvp(CCMoneyAvp.AVP_CODE) != null) {
					CCMoneyAvp ccMoneyAvp = new CCMoneyAvp(rsuAvp.getSubAvp(CCMoneyAvp.AVP_CODE));
					UnitValueAvp unitValAvp = new UnitValueAvp(ccMoneyAvp.getSubAvp(UnitValueAvp.AVP_CODE));
					ValueDigitsAvp valueDigitsAvp = new ValueDigitsAvp(unitValAvp.getSubAvp(ValueDigitsAvp.AVP_CODE));
					preRsu = valueDigitsAvp.getAsLong();
				}
			}

			MultipleServicesCreditControlAvp msccAvpgsu = new MultipleServicesCreditControlAvp(
					resCcr.getAvp(MultipleServicesCreditControlAvp.AVP_CODE));
			GrantedServiceUnitAvp gsuAvp = new GrantedServiceUnitAvp(
					msccAvpgsu.getSubAvp(GrantedServiceUnitAvp.AVP_CODE));
			if (gsuAvp != null) {
				if (gsuAvp.getSubAvp(CCMoneyAvp.AVP_CODE) != null) {
					CCMoneyAvp ccMoneyAvp = new CCMoneyAvp(gsuAvp.getSubAvp(CCMoneyAvp.AVP_CODE));
					UnitValueAvp unitValAvp = new UnitValueAvp(ccMoneyAvp.getSubAvp(UnitValueAvp.AVP_CODE));
					ValueDigitsAvp valueDigitsAvp = new ValueDigitsAvp(unitValAvp.getSubAvp(ValueDigitsAvp.AVP_CODE));
					preGsu = valueDigitsAvp.getAsLong();
				} else {
					CCServiceSpecificUnitsAvp ssuavp = new CCServiceSpecificUnitsAvp(
							gsuAvp.getSubAvp(CCServiceSpecificUnitsAvp.AVP_CODE));
					preGsu = ssuavp.getAsLong();
				}
			}
			if (preGsu != 0 && preRsu != 0 && preUsu != 0) {
				resultSSU = preGsu * (preUsu / preRsu);
			}
			usedServiceUnitAvp.addSubAvp(new CCServiceSpecificUnitsAvp(resultSSU));
		} else {
			msccAvp = new MultipleServicesCreditControlAvp(resCcr.getAvp(MultipleServicesCreditControlAvp.AVP_CODE));
			GrantedServiceUnitAvp gsuAvp = new GrantedServiceUnitAvp(msccAvp.getSubAvp(GrantedServiceUnitAvp.AVP_CODE));
			if (gsuAvp != null) {
				if (gsuAvp.getSubAvp(CCMoneyAvp.AVP_CODE) != null) {
					CCMoneyAvp ccMoneyAvp = new CCMoneyAvp(gsuAvp.getSubAvp(CCMoneyAvp.AVP_CODE));
					UnitValueAvp unitValAvp = new UnitValueAvp(ccMoneyAvp.getSubAvp(UnitValueAvp.AVP_CODE));
					ValueDigitsAvp valueDigitsAvp = new ValueDigitsAvp(unitValAvp.getSubAvp(ValueDigitsAvp.AVP_CODE));
					usedServiceUnitAvp.addSubAvp(new CCServiceSpecificUnitsAvp(valueDigitsAvp.getAsLong()));
				} else if (gsuAvp.getSubAvp(CCServiceSpecificUnitsAvp.AVP_CODE) != null) {
					usedServiceUnitAvp.addSubAvp(new CCServiceSpecificUnitsAvp(gsuAvp
							.getSubAvp(CCServiceSpecificUnitsAvp.AVP_CODE)));
				}
			}
		}
		return usedServiceUnitAvp;
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

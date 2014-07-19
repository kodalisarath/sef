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
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.util.SefCoreUtil;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.model.Operation.Type;
import com.ericsson.raso.sef.core.cg.model.TransactionStatus;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public class CommitAmountProcessor extends AbstractChargingProcessor {

	private final static Logger logger = LoggerFactory.getLogger(CommitAmountProcessor.class);
	
	@Override
	protected Integer getRequestNumber() {
		return 1;
	}

	@Override
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		if(logger.isDebugEnabled())
		logger.debug(String.format("Enter CommitAmountProcessor.preProcess, request is %s, scapCcr is %s",  request, scapCcr));
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
		}
	*/	
		session = SefCoreUtil.convertFromGSONFormat(session);
		if(logger.isDebugEnabled())
			logger.debug(String.format("Inside CommitAmountProcessor.preProcess, session after  JSON conversion is %s", session));
		
		List<Avp> responseAvps = session.getResponseAvp(Type.TRANSACATION_START);
		if(logger.isDebugEnabled())
			logger.debug(String.format("Inside CommitAmountProcessor.preProcess, responseAvps for TRANSACATION_START is %s", responseAvps));
	
		List<Avp> reqAvps = session.getRequestAvp(Type.TRANSACATION_START);
		
		if(logger.isDebugEnabled())
			logger.debug(String.format("Inside CommitAmountProcessor.preProcess, reqAvps for TRANSACATION_START is %s", reqAvps));
	
		
		UsedServiceUnitAvp usedServiceUnitAvp = getUsedServiceUnitAvpre(responseAvps, reqAvps, scapCcr);
		scapCcr.addAvp(usedServiceUnitAvp);
		request.setChargingSession(session);
		if(logger.isDebugEnabled())
		logger.debug(String.format("End CommitAmountProcessor.preProcess, response avp is %s, Modified request is %s", responseAvps,request));
	}

	@Override
	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		if(logger.isDebugEnabled())
		logger.debug(String.format("Enter CommitAmountProcessor.preProcess, request is %s, response is %s, cca is %s", request, response, cca));
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
		
		if (cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			session.setTransactionStatus(TransactionStatus.PROCESSED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		//session.setSessionInfo(this.getSessionInfo(smartSession));
		//SefCoreServiceResolver.getChargingSessionService().put(session);
		request.setChargingSession(session);
		if(logger.isDebugEnabled())
		logger.debug("End CommitAmountProcessor.postProcess, modified request is "+request);
	}

	private UsedServiceUnitAvp getUsedServiceUnitAvpre(List<Avp> resAvps, List<Avp> reqAvps, Ccr scapCcr)
			throws AvpDataException {
		if(logger.isDebugEnabled())
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
	
/*	private  Map<Type, List<Avp>> getAVPMapFromGSONString(String sessionInfo) {
		Gson gson = new Gson();
		return gson.fromJson(sessionInfo,  Map.class);		
	}
	
	private String getGSONStringFromAVPMap( Map<Type, List<Avp>> avpMap) {
		Gson gson = new Gson();
		return gson.toJson( avpMap);
	}
*/}
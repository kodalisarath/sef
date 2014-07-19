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
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.util.SefCoreUtil;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.model.Operation.Type;
import com.ericsson.raso.sef.core.cg.model.TransactionStatus;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public class ReserveAmountProcessor extends AbstractChargingProcessor {

	private final static Logger logger = LoggerFactory.getLogger(ChargeAmountProcessor.class);
	
	protected Integer getRequestNumber() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		if(logger.isDebugEnabled())
		logger.debug(String.format("Enter ReserveAmountProcessor.preProcess, request is %s, scapCcr is %s", request, scapCcr));
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
		
		session.addRequestAvp(Type.TRANSACATION_START, scapCcr.getDiameterMessage().getAvps());
		
		//session.setSessionInfo(this.getSessionInfo(smartSession));
		session = SefCoreUtil.convertToGSONFormat(session);
		request.setChargingSession(session);
		//SefCoreServiceResolver.getChargingSessionService().update(session);
		if(logger.isDebugEnabled())
		logger.debug("End ReserveAmountProcessor.preProcess  Request Object is "+request);
	}

	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		if(logger.isDebugEnabled())
		logger.debug(String.format("Enter ReserveAmountProcessor.postProcess request is %s, response is %s, cca is %s", 
				request, response, cca));
		
//		/ChargingSession session = SefCoreServiceResolver.getChargingSessionService().read(request.getSessionId());
		//SmartChargingSession smartSession = null;
		ChargingSession session = request.getChargingSession();
		
		if (session == null) {
			log.error("Invalid request with session ID: " + request.getSessionId());
			throw new AvpDataException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID.getMessage());

		}
		
		/*smartSession = this.getSmartSession(session.getSessionInfo());
		if (smartSession == null) {
			log.error("Invalid request with session ID: " + request.getSessionId());
			throw new AvpDataException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID.getMessage());
		}
		*/
		session.addResponseAvp(Type.TRANSACATION_START, response.getAvpList());
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			response.getAvpList().add(createCreditControlAvp(cca, request));
			session.setTransactionStatus(TransactionStatus.AUTHORIZED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		
		//session.setSessionInfo(this.getSessionInfo(smartSession));
		//SefCoreServiceResolver.getChargingSessionService().put(session);
		session = SefCoreUtil.convertToGSONFormat(session);
		request.setChargingSession(session);
		
		if(logger.isDebugEnabled())
		logger.debug("End ReserveAmountProcessor.postProcess request is "+request);
	}

	private MultipleServicesCreditControlAvp createCreditControlAvp(Cca cca, ChargingRequest request)
			throws AvpDataException {
		if(logger.isDebugEnabled())
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
		if(logger.isDebugEnabled())
		logger.debug("End ReserveAmountProcessor.createCreditControlAvp");
		return ccAvp;
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
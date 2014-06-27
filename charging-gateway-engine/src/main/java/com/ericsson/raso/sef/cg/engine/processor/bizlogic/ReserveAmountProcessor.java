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
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ChargingSession;
import com.ericsson.raso.sef.cg.engine.IpcCluster;
import com.ericsson.raso.sef.cg.engine.Operation.Type;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;

public class ReserveAmountProcessor extends AbstractChargingProcessor {

	private final static Logger logger = LoggerFactory.getLogger(ChargeAmountProcessor.class);
	
	protected Integer getRequestNumber() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	protected void preProcess(ChargingRequest request, Ccr scapCcr) {
		logger.debug(String.format("Enter ReserveAmountProcessor.preProcess, request is %s, scapCcr is %s", request, scapCcr));
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(request.getSessionId());
		session.addRequestAvp(Type.TRANSACATION_START, scapCcr.getDiameterMessage().getAvps());
		CgEngineContext.getIpcCluster().updateChargingSession(request.getSessionId(), session);
		logger.debug("End ReserveAmountProcessor.preProcess");
	}

	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		logger.debug(String.format("Enter ReserveAmountProcessor.postProcess request is %s, response is %s, cca is %s", 
				request, response, cca));
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(response.getSessionId());
		session.addResponseAvp(Type.TRANSACATION_START, response.getAvpList());
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			response.getAvpList().add(createCreditControlAvp(cca, request));
			session.setTransactionStatus(TransactionStatus.AUTHORIZED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		
		CgEngineContext.getIpcCluster().updateChargingSession(response.getSessionId(), session);
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



}

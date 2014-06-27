package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ChargingSession;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;

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
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(response.getSessionId());
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			session.setTransactionStatus(TransactionStatus.PROCESSED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		CgEngineContext.getIpcCluster().updateChargingSession(response.getSessionId(), session);
		logger.debug("End ChargeAmountProcessor.postProcess");
 	}
}

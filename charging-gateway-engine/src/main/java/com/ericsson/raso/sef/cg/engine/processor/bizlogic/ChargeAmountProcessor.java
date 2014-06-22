package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ChargingSession;
import com.ericsson.raso.sef.cg.engine.IpcCluster;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.charginggateway.diameter.ChargingInfo;

public class ChargeAmountProcessor extends AbstractChargingProcessor {

	@Override
	protected Integer getRequestNumber() {
		return 0;
	}

	@Override
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {

	}

	@Override
	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(response.getSessionId());
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			session.setTransactionStatus(TransactionStatus.PROCESSED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		CgEngineContext.getIpcCluster().updateChargingSession(response.getSessionId(), session);
	}
}

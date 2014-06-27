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
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ChargingSession;
import com.ericsson.raso.sef.cg.engine.IpcCluster;
import com.ericsson.raso.sef.cg.engine.Operation.Type;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;

public class CancelAmountProcessor extends AbstractChargingProcessor {
	
	private final static Logger logger = LoggerFactory.getLogger(CancelAmountProcessor.class);
	@Override
	protected Integer getRequestNumber() {
		return 1;
	}

	@Override
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		logger.debug(String.format("Enter CancelAmountProcessor.preProcess, request is %s, scapCcr is %s", request, scapCcr));
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(request.getSessionId());
		List<Avp> responseAvps = session.getResponseAvp(Type.TRANSACATION_START);

		UsedServiceUnitAvp usedServiceUnitAvp = getUsedServiceUnitAvp(responseAvps,scapCcr);
		scapCcr.addAvp(usedServiceUnitAvp);
		logger.debug(String.format("End CancelAmountProcessor.preProcess, response avp is %s", responseAvps));
	}

	@Override
	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		logger.debug(String.format("Enter CancelAmountProcessor.preProcess, request is %s, response is %s, cca is %s", 
				request, response, cca));
//		/IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(request.getSessionId());
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			session.setTransactionStatus(TransactionStatus.PROCESSED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		CgEngineContext.getIpcCluster().updateChargingSession(response.getSessionId(), session);
		logger.debug("End CancelAmountProcessor.postProcess");
	}

	private UsedServiceUnitAvp getUsedServiceUnitAvp(List<Avp> avps,Ccr scapCcr) throws AvpDataException {

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
}
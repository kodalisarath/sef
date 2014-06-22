package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import java.util.List;

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
import com.ericsson.raso.sef.charginggateway.diameter.ChargingInfo;

public class CancelAmountProcessor extends AbstractChargingProcessor {

	@Override
	protected Integer getRequestNumber() {
		return 1;
	}

	@Override
	protected void preProcess(ChargingRequest request, Ccr scapCcr) throws AvpDataException {
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(request.getSessionId());
		List<Avp> responseAvps = session.getResponseAvp(Type.TRANSACATION_START);

		UsedServiceUnitAvp usedServiceUnitAvp = getUsedServiceUnitAvp(responseAvps,scapCcr);
		scapCcr.addAvp(usedServiceUnitAvp);
	}

	@Override
	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
//		/IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = CgEngineContext.getIpcCluster().getChargingSession(request.getSessionId());
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			session.setTransactionStatus(TransactionStatus.PROCESSED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		CgEngineContext.getIpcCluster().updateChargingSession(response.getSessionId(), session);
	}

	private UsedServiceUnitAvp getUsedServiceUnitAvp(List<Avp> avps,Ccr scapCcr) throws AvpDataException {

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
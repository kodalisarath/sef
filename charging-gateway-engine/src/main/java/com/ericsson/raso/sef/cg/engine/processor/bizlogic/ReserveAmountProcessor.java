package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

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
import com.ericsson.raso.sef.charginggateway.diameter.ChargingInfo;

public class ReserveAmountProcessor extends AbstractChargingProcessor {

	protected Integer getRequestNumber() {
		return 0;
	}

	@SuppressWarnings("unchecked")
		protected void preProcess(ChargingRequest request, Ccr scapCcr) {
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = IpcCluster.getChargingSession(request.getSessionId());
		session.addRequestAvp(Type.TRANSACATION_START, scapCcr.getDiameterMessage().getAvps());
		IpcCluster.updateChargingSession(request.getSessionId(), session);
	}

	protected void postProcess(ChargingRequest request, ChargingInfo response, Cca cca) throws AvpDataException {
		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		ChargingSession session = IpcCluster.getChargingSession(response.getSessionId());
		session.addResponseAvp(Type.TRANSACATION_START, response.getAvpList());
		
		if(cca.getResultCode().intValue() == ResponseCode.DIAMETER_SUCCESS.getCode()) {
			response.getAvpList().add(createCreditControlAvp(cca, request));
			session.setTransactionStatus(TransactionStatus.AUTHORIZED);
		} else {
			session.setTransactionStatus(TransactionStatus.FAILED);
		}
		
		IpcCluster.updateChargingSession(response.getSessionId(), session);
	}

	private MultipleServicesCreditControlAvp createCreditControlAvp(Cca cca, ChargingRequest request)
			throws AvpDataException {
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
		return ccAvp;
	}



}

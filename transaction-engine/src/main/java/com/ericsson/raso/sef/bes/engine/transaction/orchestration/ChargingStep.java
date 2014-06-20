package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.csapi.wsdl.parlayx.payment.amount_charging.v2_1._interface.AmountCharging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;
import com.ericsson.raso.sef.bes.prodcat.tasks.Charging;
import com.ericsson.raso.sef.bes.prodcat.tasks.ChargingMode;
public class ChargingStep extends Step<ChargingStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;
	private static final Logger logger = LoggerFactory.getLogger(ChargingStep.class);
	ChargingStep(String stepCorrelator, Charging executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public ChargingStepResult execute() {
		
		logger.debug("Preparing for Charging");
		try {
		MonetaryUnit monetoryUnit = ((Charging)this.getExecutionInputs()).getCharging();
		ChargingMode mode = ((Charging)this.getExecutionInputs()).getMode();
		String subscriberId = ((Charging)this.getExecutionInputs()).getSubscriberId();
		String currencyCode = monetoryUnit.getIso4217CurrencyCode();
		Long amount = monetoryUnit.getAmount();
		ChargingInformation chargingInformation = new ChargingInformation();
		chargingInformation.setAmount(BigDecimal.valueOf(amount));
		chargingInformation.setCode(currencyCode);
		
		Map<String, Object> additionalInputs = ((Charging)this.getExecutionInputs()).getAdditionalInputs();
		logger.debug("additional Inputs: " + additionalInputs.toString());
		String description = "";
		if(additionalInputs!=null){
		for(Entry<String, Object> entry: additionalInputs.entrySet()){
			description = description+entry.getKey()+";"+entry.getValue()+"|";
		}
		description = description.substring(0,description.lastIndexOf("|"));
		}
		logger.debug("description is : "+description);
		chargingInformation.setDescription(description);
		String referenceCode = stepCorrelator;
		logger.debug("Charging execution mode: " + mode);
		AmountCharging request = ServiceResolver.getAmountChargingClient();
		switch(mode) {
		case CHARGE:
				request.chargeAmount(subscriberId, chargingInformation, referenceCode);
				logger.debug("ChargeStep completed");
		case REVERSE:
			//request.chargeAmount(endUserIdentifier, charge, referenceCode);
			break;
		}
		return new ChargingStepResult(null, null);
	}catch(Exception e){
		logger.debug("Exception in execution of Charging: Exception: " +  e);
		ChargingStepResult result = new ChargingStepResult(new StepExecutionException("Charging failed"), null);
		return result;
	}
	}

	@Override
	public String toString() {
		return "<ChargingStep executionInputs='" + getExecutionInputs() + "' getResult='" + getResult() + "/>";
	}

	
}
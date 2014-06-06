package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;

public class ChargingStepResult extends AbstractStepResult {
	private static final long	serialVersionUID	= -2540018090411449909L;

	private MonetaryUnit chargedAmount = null;
	
	
	ChargingStepResult(StepExecutionException resultantFault, MonetaryUnit chargedAmount) {
		super(resultantFault);
		this.chargedAmount = chargedAmount;
	}


	public MonetaryUnit getChargedAmount() {
		return chargedAmount;
	}


	public void setChargedAmount(MonetaryUnit chargedAmount) {
		this.chargedAmount = chargedAmount;
	}


	@Override
	public boolean validateResult() {
		// TODO implement when service logic is needed; for now true is good enough
		if (this.chargedAmount == null) 
			return false;
		else
			return true;
	}
	
	
	
}

package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import com.ericsson.raso.sef.bes.prodcat.tasks.Charging;

public class ChargingStep extends Step<ChargingStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;

	ChargingStep(String stepCorrelator, Charging executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public ChargingStepResult execute() {
		// TODO implement code when there is a client for Charging Adapter available
		return new ChargingStepResult(null, ((Charging)this.getExecutionInputs()).getCharging());
	}

}

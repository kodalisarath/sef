package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;

public class FulfillmentStep extends Step<FulfillmentStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;

	FulfillmentStep(String stepCorrelator, Fulfillment executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public FulfillmentStepResult execute() {
		// TODO implement code when there is a client for Fulfillment Engine available
		return new FulfillmentStepResult(null, ((Fulfillment)this.getExecutionInputs()).getAtomicProduct());
	}

}

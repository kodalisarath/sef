package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.util.Set;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;

public class FulfillmentStep extends Step<FulfillmentStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;

	FulfillmentStep(String stepCorrelator, Fulfillment executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public FulfillmentStepResult execute() {
		// TODO implement code when there is a client for Fulfillment Engine available
		Set<AtomicProduct> result = new TreeSet<AtomicProduct>();
		result.add(((Fulfillment)this.getExecutionInputs()).getAtomicProduct());
		return new FulfillmentStepResult(null, result);
	}

}

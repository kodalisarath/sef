package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;

public class PersistenceStep extends Step<PersistenceStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;

	PersistenceStep(String stepCorrelator, Persistence<?> executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public PersistenceStepResult execute() {
		// TODO implement code when there is a client for Fulfillment Engine available
		return new PersistenceStepResult(null, ((Persistence<?>)this.getExecutionInputs()).getToSave());
	}
	
	@Override
	public String toString() {
		return "<PersistenceStep executionInputs='" + getExecutionInputs() + "' getResult='" + getResult() + "/>";
	}


}

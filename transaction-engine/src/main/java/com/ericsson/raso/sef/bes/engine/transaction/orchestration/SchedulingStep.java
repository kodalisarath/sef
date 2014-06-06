package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import com.ericsson.raso.sef.bes.prodcat.tasks.Future;

public class SchedulingStep extends Step<SchedulingStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;

	SchedulingStep(String stepCorrelator, Future executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public SchedulingStepResult execute() {
		// TODO implement code when there is a client for Notification Engine available
		return new SchedulingStepResult(null, true);
	}
	
	@Override
	public String toString() {
		return "<SchedulingStep executionInputs='" + getExecutionInputs() + "' getResult='" + getResult() + "/>";
	}



}

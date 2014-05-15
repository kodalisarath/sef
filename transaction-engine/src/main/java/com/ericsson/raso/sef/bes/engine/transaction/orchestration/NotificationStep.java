package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import com.ericsson.raso.sef.bes.prodcat.tasks.Notification;

public class NotificationStep extends Step<NotificationStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;

	NotificationStep(String stepCorrelator, Notification executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public NotificationStepResult execute() {
		// TODO implement code when there is a client for Notification Engine available
		return new NotificationStepResult(null, true);
	}

}

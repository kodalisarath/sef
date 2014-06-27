package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.tasks.Future;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.Period;
import com.ericsson.raso.sef.core.config.PeriodUnit;
import com.ericsson.sef.scheduler.command.PurchaseRenewalCommand;

public class SchedulingStep extends Step<SchedulingStepResult> {
	private static final long serialVersionUID = 6645187522590773212L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingStep.class);

	SchedulingStep(String stepCorrelator, Future executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public SchedulingStepResult execute() {
		Future future = (Future) getExecutionInputs();

		String requestId = RequestContextLocalStore.get().getRequestId();
		if (requestId == null) {
			LOGGER.error("Request Identifier is not set.");
			return new SchedulingStepResult(new StepExecutionException("No requestId assigned."), false);
		}

		switch (future.getMode()) {
		case SCHEDULE:
			switch (future.getEvent()) {
			case PURCHASE:
			case RENEWAL:
				try {
					new PurchaseRenewalCommand(future.getSubscriberId(), future.getOfferId(), null, requestId,
							future.getMetas(), 1, new Period(
									(int) ((future.getSchedule() - System.currentTimeMillis()) / 1000),
									PeriodUnit.SECOND)).execute();
					return new SchedulingStepResult(null, true);
				} catch (SmException e) {
					LOGGER.error(e.getMessage(), e);
					return new SchedulingStepResult(new StepExecutionException(e.getMessage(), e), false);
				}
			default:
			}
		case CANCEL:
		}
		return new SchedulingStepResult(null, false);
	}

	@Override
	public String toString() {
		return "<SchedulingStep executionInputs='" + getExecutionInputs() + "' getResult='" + getResult() + "/>";
	}

}

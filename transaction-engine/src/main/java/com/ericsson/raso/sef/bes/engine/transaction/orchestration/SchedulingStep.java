package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.tasks.Future;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.scheduler.command.CancelSubscriptionLifeCycleCommand;
import com.ericsson.sef.scheduler.command.SubscriptionLifeCycleCommand;

public class SchedulingStep extends Step<SchedulingStepResult> {
	private static final long serialVersionUID = 6645187522590773212L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SchedulingStep.class);

	SchedulingStep(String stepCorrelator, Future executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public SchedulingStepResult execute() {
		Future future = (Future) getExecutionInputs();

//		String requestId = RequestContextLocalStore.get().getRequestId();
//		if (requestId == null) {
//			LOGGER.error("Request Identifier is not set.");
//			return new SchedulingStepResult(new StepExecutionException(
//					"No requestId assigned."), false);
//		}

		switch (future.getMode()) {
		case SCHEDULE:

			try {
				new SubscriptionLifeCycleCommand(future.getEvent().name(),
						future.getOfferId(), future.getSubscriberId(),
						future.getMetas(), future.getSchedule()).execute();
				return new SchedulingStepResult(null, true);

			} catch (SmException e) {
				LOGGER.error(e.getMessage(), e);
				return new SchedulingStepResult(new StepExecutionException(
						e.getMessage(), e), false);
			}

		case CANCEL:

			try {
				new CancelSubscriptionLifeCycleCommand(
						future.getEvent().name(), future.getOfferId(),
						future.getSubscriberId()).execute();
				return new SchedulingStepResult(null, true);

			} catch (SmException e) {
				LOGGER.error(e.getMessage(), e);
				return new SchedulingStepResult(new StepExecutionException(
						e.getMessage(), e), false);
			}

		}
		return new SchedulingStepResult(null, false);
	}

	@Override
	public String toString() {
		return "<SchedulingStep executionInputs='" + getExecutionInputs()
				+ "' getResult='" + getResult() + "/>";
	}

}

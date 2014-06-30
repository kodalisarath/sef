package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.tasks.Future;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.scheduler.command.CancelSubscriptionLifeCycleCommand;
import com.ericsson.sef.scheduler.command.SubscriptionLifeCycleCommand;

public class SchedulingStep extends Step<SchedulingStepResult> {
	private static final long serialVersionUID = 6645187522590773212L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingStep.class);

	SchedulingStep(String stepCorrelator, Future executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public SchedulingStepResult execute() {
		Future future = (Future) getExecutionInputs();
		LOGGER.debug("Scheduling engaged. Future: " + future);


		switch (future.getMode()) {
		case SCHEDULE:

			try {
				
				SubscriptionLifeCycleCommand schedule =  new SubscriptionLifeCycleCommand(future.getEvent().name(),
																			future.getOfferId(), future.getSubscriberId(),
																			future.getMetas(), future.getSchedule());
				schedule.execute();
				LOGGER.debug("Scheduling successful for: " + schedule);
				return new SchedulingStepResult(null, true);

			} catch (SmException e) {
				LOGGER.error("Failed schedule. Cause: " + e.getMessage(), e);
				return new SchedulingStepResult(new StepExecutionException(e.getMessage(), e), false);
			}

		case CANCEL:

			try {
				new CancelSubscriptionLifeCycleCommand(
						future.getEvent().name(), future.getOfferId(),
						future.getSubscriberId()).execute();
				return new SchedulingStepResult(null, true);

			} catch (SmException e) {
				LOGGER.error("Failed cancel schedule. Cause: " + e.getMessage(), e);
				return new SchedulingStepResult(new StepExecutionException(e.getMessage(), e), false);
			}

		}
		return new SchedulingStepResult(null, false);
	}

	@Override
	public String toString() {
		return "SchedulingStep [stepCorrelator=" + stepCorrelator 
				+ ", getExecutionInputs()=" + getExecutionInputs() 
				+ ", getResult()=" + getResult() 
				+ ", getFault()=" + getFault() + "]";
	}


	private long scaleDownTimeForTesting(long requestedTimeInMilliSeconds)
	{
		long currentTimeInMilliSeconds = Calendar.getInstance().getTimeInMillis();
		long timeDifference = currentTimeInMilliSeconds - currentTimeInMilliSeconds;
		long scaledDownTime = timeDifference/30 ; //ScaledDownTime 1 Hour = 2 Minute.
		return(currentTimeInMilliSeconds + scaledDownTime);
	}

}

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
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SchedulingStep.class);

	SchedulingStep(String stepCorrelator, Future executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public SchedulingStepResult execute() {
		Future future = (Future) getExecutionInputs();
		LOGGER.debug("Scheduling engaged. Future: " + future);

		try {
			switch (future.getMode()) {
			case SCHEDULE:
				LOGGER.debug("*************************************************SCHEDULE START*******************************************************************");
				SubscriptionLifeCycleCommand subscriptionSchedule = new SubscriptionLifeCycleCommand(
						future.getEvent().name(),
						future.getOfferId() /* SubscriptionID */,
						future.getOfferId()/* offer Id */,
						future.getSubscriberId(), future.getMetas(),
						future.getSchedule());
				subscriptionSchedule.execute();
				// this.wait(45000);

				LOGGER.debug("*************************************************SCHEDULE START*******************************************************************");
				return new SchedulingStepResult(null, true);
			case CANCEL:
				LOGGER.debug("*************************************************CANCEL START*******************************************************************");
				CancelSubscriptionLifeCycleCommand cancelSubscriptionLifeCycleCommand = new CancelSubscriptionLifeCycleCommand(
						future.getEvent().name(), future.getOfferId(),
						future.getSubscriberId());
				cancelSubscriptionLifeCycleCommand.execute();
				LOGGER.debug("*************************************************CANCEL END*******************************************************************");
				return new SchedulingStepResult(null, true);
			}
		} catch (SmException smException) {
			LOGGER.error("SmException is  ",smException);
		}
		return new SchedulingStepResult(null, false);
	}

	@Override
	public String toString() {
		return "SchedulingStep [stepCorrelator=" + stepCorrelator
				+ ", getExecutionInputs()=" + getExecutionInputs()
				+ ", getResult()=" + getResult() + ", getFault()=" + getFault()
				+ "]";
	}
	
	
	
/**This methd is just added for scaling down the schedule time.**/
	private long scaleDownTimeForTesting(long requestedTimeInMilliSeconds)

	{

		long currentTimeInMilliSeconds = Calendar.getInstance()
				.getTimeInMillis();

		long timeDifference = requestedTimeInMilliSeconds
				- currentTimeInMilliSeconds;
		long scaledDownTime = timeDifference / 30; // ScaledDownTime 1 Hour = 2
													// Minute.

		LOGGER.debug("requestedTimeInMilliSeconds "
				+ requestedTimeInMilliSeconds + " ,currentTimeInMilliSeconds "
				+ currentTimeInMilliSeconds + " ,timeDifference "
				+ timeDifference + " ,scaledDownTime " + scaledDownTime
				+ " ,finalTime " + (currentTimeInMilliSeconds + scaledDownTime));
		return (currentTimeInMilliSeconds + scaledDownTime);
	}

}

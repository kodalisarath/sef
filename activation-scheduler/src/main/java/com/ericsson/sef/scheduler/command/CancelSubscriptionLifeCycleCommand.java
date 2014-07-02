package com.ericsson.sef.scheduler.command;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;

public class CancelSubscriptionLifeCycleCommand implements Command<Void> {

	private static Logger log = LoggerFactory
			.getLogger(CancelSubscriptionLifeCycleCommand.class);

	private String lifeCycleEvent = null;
	private String offerId = null;
	private String subscriberId = null;

	// private String jobName;

	public CancelSubscriptionLifeCycleCommand(String lifeCycleEvent,
			String offerId, String subscriberId) {
		log.debug("CancelSubscriptionLifeCycleCommand  Constructor  lifeCycleEvent: "
				+ lifeCycleEvent
				+ " ,offerId: "
				+ offerId
				+ " ,subscriberId: "
				+ subscriberId);

		this.lifeCycleEvent = lifeCycleEvent;
		this.offerId = offerId;
		this.subscriberId = subscriberId;

	}

	@Override
	public Void execute() throws SmException {

		
		log.debug("CancelSubscriptionLifeCycleCommand  Execute Method: "
				);
		List<String> scheduledRequestList =null;
		try
		{
			String msisdn = subscriberId;
		 scheduledRequestList = (List<String>) SefCoreServiceResolver
				.getScheduleRequestService().getJobIdByOfferId(msisdn,
						offerId, lifeCycleEvent);

		}
		catch(Exception e)
		{
			log.error("Excetpion Occured at CancelSubscriptionLifeCycleCommand getJobIdByOfferId",e);
		}
		log.debug("CancelSubscriptionLifeCycleCommand  jobsName: "
				+ scheduledRequestList);

		if (scheduledRequestList != null)

		{
			SchedulerService scheduler = SchedulerContext.getSchedulerService();

			for (String jobId : scheduledRequestList) {

				
				log.debug("CancelSubscriptionLifeCycleCommand  Job About to be Cancelled: "
						+ jobId);

				try {
					scheduler.deleteJob(jobId);
				} catch (SchedulerException e) {
					log.error("Cannot cancel the Job " + lifeCycleEvent
							+ " offerId: " + offerId + " subscriberId: "
							+ subscriberId + ":" + e.getMessage(), e);
					throw new SmException(e);
				}
			}
		} else {
			log.debug("CancelSubscriptionLifeCycleCommand Job Not Found: lifeCycleEvent: "
					+ lifeCycleEvent
					+ " offerId: "
					+ offerId
					+ " subscriberId: " + subscriberId);
		}
		return null;
	}

}

package com.ericsson.sef.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;

public class SubscriptionLifeCycleJob implements Job {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String jobId = context.getJobDetail().getKey().getName();
		log.info("SubscriptionLifeCycleJob executing job with job ID(Modified Logs Details  : " + jobId
				+ " Job details : "
				+ context.getJobDetail().getJobDataMap().toString());
		
		log.info("About to fetch from ScheduledRequestMapper ");
		
		ScheduledRequest scheduledRequest =null;
		try
		{
		 scheduledRequest =SefCoreServiceResolver.getScheduleRequestService().getScheduledRequestByJobId(jobId);

		}
		catch(Exception e)
		{
			log.error("Exception while fetching the job details "+e);
		}
		log.debug("scheduledRequest "+scheduledRequest);
		
		if (scheduledRequest == null) {
			log.error("Job is null for the given executing job with job ID:  "
					+ jobId + " Scheduled Job cannot Execute ");
			throw new RuntimeException(
					"Job is null for the given executing job with job ID: "
							+ jobId + " Scheduled Job cannot Execute ");
		}
		try {

			log.debug("LifeCycle Name is "+scheduledRequest.getLifeCycleEvent().name() + " ,Offer id: "+scheduledRequest.getOfferId() +" ,MSISDN: "+scheduledRequest.getMsisdn() +" ,Metas: "+scheduledRequest.getRequestMetas());
			
			
			switch (scheduledRequest.getLifeCycleEvent().name()) {

			
			
			case "PURCHASE":
				TransactionEngineHelper
						.purchase(
								scheduledRequest.getOfferId(),
								scheduledRequest.getMsisdn(),
								TransactionEngineHelper
										.convertScheduledReqMetasToAPIMetas(scheduledRequest
												.getRequestMetas()));
				break;

			case "RENEWAL":
				TransactionEngineHelper
						.renew(scheduledRequest.getMsisdn(),
								TransactionEngineHelper
										.convertScheduledReqMetasToAPIMetas(scheduledRequest
												.getRequestMetas()));
				break;

			case "EXPIRY":

				TransactionEngineHelper
						.expiry(scheduledRequest.getMsisdn(),
								TransactionEngineHelper
										.convertScheduledReqMetasToAPIMetas(scheduledRequest
												.getRequestMetas()));

				break;

			case "TERMINATE":

				TransactionEngineHelper
						.terminate(
								scheduledRequest.getMsisdn(),
								TransactionEngineHelper
										.convertScheduledReqMetasToAPIMetas(scheduledRequest
												.getRequestMetas()));
				break;

			default:
				log.error("Purchase Job Invalid LifeCycleEvent "
						+ scheduledRequest.getLifeCycleEvent());
				break;

			}
		} catch (SmException smException) {
			log.error("Error Executing the Job for offerID: "
					+ scheduledRequest.getOfferId() + " LifeCycle Event "
					+ scheduledRequest.getLifeCycleEvent().name(), smException);
		}
	}
}
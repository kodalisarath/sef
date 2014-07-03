package com.ericsson.sef.scheduler;

import org.apache.camel.ProducerTemplate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestStatus;
import com.ericsson.raso.sef.core.smpp.SmppMessage;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;

/*Expiry Notification Command and Job design are told by Sathya and Navneet,  implemented as per their input on 03-July-2014 2 AM at Mandrin Hotel*/
public class ExpiryNotificationJob implements Job {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String jobId =null;
		try {
			jobId = context.getJobDetail().getKey().getName();
			 
			log.info("ExpiryNotificationJob executing job with job ID : "
					+ jobId );

			 ScheduledRequest scheduledRequest = SefCoreServiceResolver
					.getScheduleRequestService().getScheduledRequestByJobId(
							jobId);
			
			log.info("ExpiryNotificationJob executing job scheduled_Request is "
					+ scheduledRequest );
			
			
			if (scheduledRequest == null) {
				log.error("ExpiryNotificationJob job is null for the given executing job with job ID: "
						+ jobId);
				throw new RuntimeException(
						"ExpiryNotificationJob job is null for the given executing job with job ID: "
								+ jobId);
			}

			if (scheduledRequest.getStatus() != ScheduledRequestStatus.SCHEDLUED) {
				log.error("ExpiryNotification job is not set to scheduled for the given executing job with job ID: "
						+ jobId);
				throw new RuntimeException(
						"ExpiryNotification job is not set to scheduled for the given executing job with job ID: "
								+ jobId);
			}
			SubscriberInfo subscriberInfo = TransactionEngineHelper
					.getSubscriberInfo(scheduledRequest.getUserId());
			if (subscriberInfo == null)
				throw new RuntimeException(
						"User with user ID  does not exist anymore: "
								+ scheduledRequest.getUserId());
			SmppMessage message = new SmppMessage();
			message.setDestinationMsisdn(scheduledRequest.getMsisdn());
			
			log.debug("scheduledRequest.getLifeCycleEvent().name() is "+scheduledRequest
					.getLifeCycleEvent().name() );
			if (HelperConstant.NOTIFICATION_PRE_EXPIRY.equals(scheduledRequest
					.getLifeCycleEvent().name()))
				message.setMessageBody(HelperConstant.PRE_EXPIRY_PREFIX + "_"
						+ scheduledRequest.getResourceId());
			else
				message.setMessageBody(HelperConstant.ON_EXPIRY_PREFIX + "_"
						+ scheduledRequest.getResourceId());
			
			log.debug("Posting the message to Notification QUeue MSISDN is "+scheduledRequest.getMsisdn() +" message body is "+message.getMessageBody());
			
			ProducerTemplate template = SchedulerContext.getCamelContext()
					.createProducerTemplate();
			template.sendBody("activemq:queue:notification", message);
			scheduledRequest.setStatus(ScheduledRequestStatus.COMPLETED);
			SefCoreServiceResolver.getScheduleRequestService()
					.upadteScheduledRequestStatus(scheduledRequest);
			
			log.debug("Updated Scheduled Request Status successfully.  Message Posted to the queue.");
		} catch (Exception e) {

			log.error("ExpiryNotificationJob JOB Failed JobID is " + jobId, e);

		}

	}
}
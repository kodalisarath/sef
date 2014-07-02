package com.ericsson.sef.scheduler;

import java.util.Collection;

import org.apache.camel.ProducerTemplate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestStatus;
import com.ericsson.raso.sef.core.smpp.SmppMessage;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;

public class ExpiryNotificationJob implements Job {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	String jobId  =null;
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
	try{	
		 jobId = context.getJobDetail().getKey().getName();
		log.info("ExpiryNotificationJob executing job with job ID : " + jobId
				+ " JOb details : "
				+ context.getJobDetail().getJobDataMap().toString());

		final ScheduledRequest scheduledRequest = SefCoreServiceResolver
				.getScheduleRequestService().getScheduledRequestByJobId(jobId);
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

		Collection<ScheduledRequestMeta> metas = SefCoreServiceResolver
				.getScheduleRequestService().getScheduledRequestMetas(
						scheduledRequest.getId());

		for (ScheduledRequestMeta m : metas) {
			if (m.getKey().startsWith(HelperConstant.NOTIFICATION_MESSAGE)) {
				SmppMessage message = new SmppMessage();
				message.setDestinationMsisdn(scheduledRequest.getMsisdn());
				message.setMessageBody("0," + m.getValue());
				ProducerTemplate template = SchedulerContext.getCamelContext()
						.createProducerTemplate();
				template.sendBody("activemq:queue:notification", message);
			}
		}

		scheduledRequest.setStatus(ScheduledRequestStatus.COMPLETED);
		SefCoreServiceResolver.getScheduleRequestService()
				.upadteScheduledRequestStatus(scheduledRequest);
	}catch(Exception e)
	{
		
		log.error("ExpiryNotificationJob JOB Failed JobID is " +jobId  ,e);
	
	}

	
}
}
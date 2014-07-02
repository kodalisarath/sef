package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ObsoleteCodeDbSequence;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestStatus;
import com.ericsson.raso.sef.core.db.model.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.core.db.service.ScheduleRequestService;
import com.ericsson.sef.scheduler.ErrorCode;
import com.ericsson.sef.scheduler.HelperConstant;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;
import com.ericsson.sef.scheduler.SubscriptionLifeCycleJob;

public class SubscriptionLifeCycleCommand implements Command<Void> {

	private static Logger log = LoggerFactory
			.getLogger(SubscriptionLifeCycleCommand.class);

	private String event = null;
	private String offerId = null;
	private String subscriberId = null;
	private String subscribtionId =null;
	private Long schedule;
	private Map<String, Object> metas;

	public SubscriptionLifeCycleCommand(String event, String subscriptionId, String offerId,
			String subscriberId, Map<String, Object> metas, Long schedule) {
		log.debug("SubscriptionLifeCycleCommand  Constructor  event: " + event
				+ " offerId: " + offerId + " subscriberId: " + subscriberId
				+ " schedule: " + schedule + " metas:" + metas);

		this.event = event;
		this.offerId = offerId;
		this.subscriberId = subscriberId;
		this.metas = metas;
		this.schedule = schedule;
		this.subscribtionId=subscriptionId;
	}

	@Override
	public Void execute() throws SmException {
		try {
			log.debug("Calling SubscriptionLifeCycleCommand execute method.");
			final ScheduleRequestService mapper = SefCoreServiceResolver
					.getScheduleRequestService();

			ObsoleteCodeDbSequence sequence = mapper
					.scheduledRequestSequence(UUID.randomUUID().toString());
			final long id = sequence.getSeq();
			// Date scheduleTime = new Date(schedule);
			Calendar scheduledTime = Calendar.getInstance();
			//scheduledTime.add(Calendar.SECOND, 20);
			scheduledTime.setTimeInMillis(schedule);
			final ScheduledRequest request = new ScheduledRequest();
			request.setCreated(new Date());
			request.setStatus(ScheduledRequestStatus.SCHEDLUED);
			request.setResourceId(HelperConstant.RESOURCE_NAME);
			request.setId(id);
			if ("NEW_PURCHASE".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.NEW_PURCHASE);
			else if ("RENEWAL".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.RENEWAL);
			else if ("EXPIRY".equals(event))
			{
				scheduledTime.add(Calendar.SECOND, -30);
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.EXPIRY);
			}
			else if ("TERMINATION".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.TERMINATION);
			else if ("PRE_EXPIRY".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.PRE_EXPIRY);
			request.setMsisdn(subscriberId);
			request.setUserId(subscriberId);
			request.setOfferId(offerId);
			
		

			Calendar now = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSZ");
			if (now.after(scheduledTime)) {
				log.error("Requested Scheduled Time is already past...Cannot Proceed."
						+ formatter.format(scheduledTime.getTime()));
				throw new SmException(
						"Requested Scheduled Time is already past...Cannot Proceed",
						ErrorCode.internalServerError);
			}
			
			// cal.add(Calendar.SECOND, 15);
			//Date quartzTime = scheduledTime.getTime();
			request.setScheduleTime(scheduledTime.getTime());

			log.debug("Preparing for Quartz...scheduleTime is " + scheduledTime.getTime());
			String jobId = event + '-' + String.valueOf(id);
			log.debug("jobID: " + jobId);
			request.setJobId(jobId);

			log.debug("Quartz request: " + request);

			JobDetail job = newJob(SubscriptionLifeCycleJob.class)
					.withIdentity(jobId).build();

			log.debug("Quartz Job Created: Key is " + job.getKey());

			log.debug("Quartz Job Created: Trigger Event Key is " + event + '-'
					+ String.valueOf(id) + " scheduleTime=  " + formatter.format(scheduledTime.getTime()));

			/*
			 * Trigger trigger = newTrigger() .withIdentity(event + '-' +
			 * String.valueOf(id)) .startAt(scheduleTime)
			 * .withSchedule(simpleSchedule
			 * ().withIntervalInMilliseconds(10).withRepeatCount(0)).build();
			 */

			SimpleTrigger trigger = (SimpleTrigger) newTrigger()
					.withIdentity(event + '-' + String.valueOf(id))
					.startAt(scheduledTime.getTime()) // some Date
					.forJob(job.getKey()) // identify job with name, group
											// strings
					.build();

			log.debug("See whats in this trigger: " + trigger);

			mapper.insertScheduledRequest(request);
			ScheduledRequestMeta requestMeta =null;
			for (Entry<String, Object> meta : metas.entrySet()) {
				if (meta.getKey().equalsIgnoreCase(HelperConstant.REQUEST_ID))
					continue;
				if (meta.getKey().equalsIgnoreCase(HelperConstant.USECASE))
					continue;
				if (meta.getKey().equalsIgnoreCase(
						HelperConstant.SUBSCRIPTION_LIFE_CYCLE_EVENT))
					continue;
				
			
				requestMeta = new ScheduledRequestMeta();
				requestMeta.setScheduledRequestId(id);
				requestMeta.setKey(meta.getKey());
				requestMeta.setValue(String.valueOf(meta.getValue()));
				log.debug("Checking Schedule Meta before insert: "
						+ requestMeta);
				mapper.insertScheduledRequestMeta(requestMeta);
				log.debug("schedule Meta inserted into db graceful!!");
			}

			
			requestMeta = new ScheduledRequestMeta();
			requestMeta.setScheduledRequestId(id);
			requestMeta.setKey("SUBSCRIBTION_ID");
			requestMeta.setValue(subscribtionId);
			log.debug("Checking Schedule Meta before insert: "
					+ requestMeta);
			mapper.insertScheduledRequestMeta(requestMeta);
			log.debug("schedule inserted into db graceful!!");
			
			
			SchedulerService scheduler = SchedulerContext.getSchedulerService();
			scheduler.scheduleJob(job, trigger);
			log.debug("QUartz engaged and scheduled!!");

		} catch (Exception e) {
			log.error("error creating SubscriptionLifeCycleCommand job.", e);
			throw new SmException(e);
		}
		return null;
	}
}

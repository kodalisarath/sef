package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ObsoleteCodeDbSequence;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.model.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.core.db.service.ScheduleRequestService;
import com.ericsson.sef.scheduler.HelperConstant;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;
import com.ericsson.sef.scheduler.SubscriptionLifeCycleJob;

public class SubscriptionLifeCycleCommand implements Command<Void> {

	private static Logger log = LoggerFactory.getLogger(SubscriptionLifeCycleCommand.class);
	
	private String event = null;
	private String offerId = null;
	private String subscriberId = null;
	private Long schedule;
	private Map<String, Object> metas;

	public SubscriptionLifeCycleCommand( String event, String offerId,
			String subscriberId, Map<String, Object> metas, Long schedule) {
		log.debug("SubscriptionLifeCycleCommand  Constructor  event: "
				+ event + " offerId: " + offerId + " subscriberId: "
				+ subscriberId + " schedule: " + schedule + " metas:" + metas);
		
		this.event = event;
		this.offerId = offerId;
		this.subscriberId = subscriberId;
		this.metas = metas;
		this.schedule = schedule;
	}

	@Override
	public Void execute() throws SmException {
		try {
			log.debug("Calling SubscriptionLifeCycleCommand execute method.");
			final ScheduleRequestService mapper = SefCoreServiceResolver.getScheduleRequestService();
			ObsoleteCodeDbSequence sequence = mapper.scheduledRequestSequence(UUID.randomUUID().toString());
			final long id = sequence.getSeq();
			final ScheduledRequest request = new ScheduledRequest();
			request.setCreated(new Date());
			request.setId(id);
			if ("NEW_PURCHASE".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.NEW_PURCHASE);
			else if ("RENEWAL".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.RENEWAL);
			else if ("EXPIRY".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.EXPIRY);
			else if ("TERMINATION".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.TERMINATION);
			else if ("PRE_EXPIRY".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.PRE_EXPIRY);
			request.setMsisdn(subscriberId);
			request.setUserId(subscriberId);
			request.setOfferId(offerId);
			Date scheduleTime = new Date(schedule);
			request.setScheduleTime(scheduleTime);
			
			log.debug("Preparing for Quartz...");
			String jobId = event + '-' + String.valueOf(id);
			log.debug("jobID: " + jobId);
			request.setJobId(jobId);
			
			log.debug("Quartz request: " + request);
			
			JobDetail job = newJob(SubscriptionLifeCycleJob.class).withIdentity(jobId).build();
			Trigger trigger = newTrigger()
					.withIdentity(event + '-' + String.valueOf(id))
						.startAt(scheduleTime)
							.withSchedule(simpleSchedule().withIntervalInMilliseconds(schedule).withRepeatCount(0)).build();
			log.debug("See whats in this trigger: " + trigger);
			
			mapper.insertScheduledRequest(request);
			for (Entry<String, Object> meta : metas.entrySet()) {
				if (meta.getKey().equalsIgnoreCase(HelperConstant.REQUEST_ID))
					continue;
				if (meta.getKey().equalsIgnoreCase(HelperConstant.USECASE))
					continue;
				if (meta.getKey().equalsIgnoreCase(
						HelperConstant.SUBSCRIPTION_LIFE_CYCLE_EVENT))
					continue;
				ScheduledRequestMeta requestMeta = new ScheduledRequestMeta();
				requestMeta.setScheduledRequestId(id);
				requestMeta.setKey(meta.getKey());
				requestMeta.setValue(String.valueOf(meta.getValue()));
				log.debug("Checking Schedule Meta before insert: " + requestMeta);
				mapper.insertScheduledRequestMeta(requestMeta);
				log.debug("schedule inserted into db graceful!!");
			}
			
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

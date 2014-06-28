package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.joda.time.DateTime;
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
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscription.response.HelperConstant;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.scheduler.PurchaseJob;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;

public class PurchaseCommand implements Command<Void> {

	private static Logger log = LoggerFactory.getLogger(PurchaseCommand.class);
	private String mode = null;
	private String event = null;
	private String offerId = null;
	private String subscriberId = null;
	private Long schedule;
	private Map<String, Object> metas;

	public PurchaseCommand(String mode, String event, String offerId,
			String subscriberId, Map<String, Object> metas, Long schedule) {
		log.debug("PurchaseCommand  Constructor mode: " + mode + " event: "
				+ event + " offerId: " + offerId + " subscriberId: "
				+ subscriberId + " schedule: " + schedule + " metas:" + metas);
		this.mode = mode;
		this.event = event;
		this.offerId = offerId;
		this.subscriberId = subscriberId;
		this.metas = metas;
		this.schedule = schedule;
	}

	@Override
	public Void execute() throws SmException {
		try {
			log.debug("Calling PurchaseCommand Execute Method.");
			Subscriber subscriber = null;
			try {
				SubscriberInfo subscriberInfo = TransactionEngineHelper
						.getSubscriberInfo(subscriberId);
				log.debug("subscriberInfo returned is " + subscriberInfo);
				subscriber = subscriberInfo.getSubscriber();
				if (subscriber == null) {
					throw new RuntimeException("User with userID: "
							+ subscriberId + " does not exist anymore: ");
				}
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			final ScheduleRequestService mapper = SefCoreServiceResolver
					.getScheduleRequestService();
			ObsoleteCodeDbSequence sequence = mapper
					.scheduledRequestSequence(UUID.randomUUID().toString());
			final long id = sequence.getSeq();
			final ScheduledRequest request = new ScheduledRequest();
			request.setCreated(new DateTime());
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
			request.setUserId(subscriber.getUserId());
			request.setOfferId(offerId);
			DateTime scheduleTime = new DateTime(schedule);
			request.setScheduleTime(scheduleTime);
			String jobId = event + '-' + String.valueOf(id);
			request.setJobId(jobId);
			JobDetail job = newJob(PurchaseJob.class).withIdentity(jobId)
					.build();
			Trigger trigger = newTrigger()
					.withIdentity(event + '-' + String.valueOf(id))
					.startAt(scheduleTime.toDate())
					.withSchedule(
							simpleSchedule().withIntervalInMilliseconds(
									schedule).withRepeatCount(0)).build();
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
				mapper.insertScheduledRequestMeta(requestMeta);
			}
			SchedulerService scheduler = SchedulerContext.getSchedulerService();
			scheduler.scheduleJob(job, trigger);
		} catch (Exception e) {
			log.error("error creating expiry job.", e);
			throw new SmException(e);
		}
		return null;
	}
}

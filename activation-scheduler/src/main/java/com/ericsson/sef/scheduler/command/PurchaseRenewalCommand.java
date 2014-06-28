package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.joda.time.DateTime;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.Period;
import com.ericsson.raso.sef.core.db.model.ObsoleteCodeDbSequence;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.model.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.core.db.service.ScheduleRequestService;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.HelperConstant;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.scheduler.OfferRenewalJob;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;
//import com.ericsson.sm.core.Command;
//import com.ericsson.sm.core.Meta;
//import com.ericsson.sm.core.Period;
//import com.ericsson.sm.core.SmConstants;
//import com.ericsson.sm.core.SmException;
//import com.ericsson.sm.core.db.mapper.ScheduledRequestMapper;
//import com.ericsson.sm.core.db.model.ScheduledRequest;
//import com.ericsson.sm.core.db.model.ScheduledRequestMeta;
//import com.ericsson.sm.core.db.model.SmSequence;
//import com.ericsson.sm.core.db.model.SubscriptionLifeCycleEvent;
import com.hazelcast.core.ISemaphore;

public class PurchaseRenewalCommand implements Command<Void> {

	private static Logger log = LoggerFactory.getLogger(PurchaseRenewalCommand.class);

	private String msisdn;
	private String offerId;
	private String purchaseId;
	private String requestId;
	private int renewalFrequency;
	private Period renewalPeriod;
	private final Map<String, Object> metas;

	public PurchaseRenewalCommand(String msisdn, String offerId, String purchaseId, String requestId,
			Map<String, Object> metas, int renewalFrequency, Period renewalPeriod) {
		this.msisdn = msisdn;
		this.offerId = offerId;
		this.purchaseId = purchaseId;
		this.requestId = requestId;
		this.metas = metas;
		this.renewalFrequency = renewalFrequency;
		this.renewalPeriod = renewalPeriod;
	}

	@Override
	public Void execute() throws SmException {
		try {
			/*
			 * UserProfileService userProfileService =
			 * SchedulerContext.getUserProfileService(); final String userId =
			 * userProfileService.getUserId(msisdn);
			 */
			log.debug("Calling PurchaseRenewalCommand.....");
			//
			Subscriber subscriber = null;
			try {
				String requestId = RequestContextLocalStore.get().getRequestId();
			//	List<Meta> metaList = new ArrayList<Meta>();
			//	metaList.add(new Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
				SubscriberInfo subscriberInfo = TransactionEngineHelper.getSubscriberInfo(msisdn);

				log.debug("subscriberInfo returned is " + subscriberInfo);
				subscriber = subscriberInfo.getSubscriber();
				if (subscriber == null) {
					throw new RuntimeException("User with userID: " + msisdn + " does not exist anymore: ");
				}
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			//
			log.debug("invoking ScheduledRequestMapper...");
			final ScheduleRequestService mapper = SefCoreServiceResolver.getScheduleRequestService();
			ObsoleteCodeDbSequence sequence = mapper.scheduledRequestSequence(UUID.randomUUID().toString());
			final long id = sequence.getSeq();
			final ScheduledRequest request = new ScheduledRequest();
			request.setCreated(new DateTime());
			request.setId(id);
			request.setPurchaseId(purchaseId);
			request.setLifeCycleEvent(SubscriptionLifeCycleEvent.RENEWAL);
			request.setMsisdn(msisdn);
			request.setUserId(subscriber.getUserId());
			request.setOfferId(offerId);
			request.setRequestId(requestId);

			DateTime scheduleTime = new DateTime();

			request.setScheduleTime(scheduleTime);

			String jobId = SubscriptionLifeCycleEvent.RENEWAL.name() + '-' + String.valueOf(id);
			request.setJobId(jobId);
			JobDetail job = newJob(OfferRenewalJob.class).withIdentity(jobId).build();

			Trigger trigger = newTrigger()
					.withIdentity(SubscriptionLifeCycleEvent.RENEWAL.name() + '-' + String.valueOf(id))
					.startAt(scheduleTime.plus(renewalPeriod.getPeriodInMills()).toDate())
					.withSchedule(
							simpleSchedule().withIntervalInMilliseconds(renewalPeriod.getPeriodInMills())
									.withRepeatCount(renewalFrequency - 1)).build();

			mapper.insertScheduledRequest(request);
			for (Entry<String, Object> meta : metas.entrySet()) {
				if (meta.getKey().equalsIgnoreCase(HelperConstant.REQUEST_ID))
					continue;
				if (meta.getKey().equalsIgnoreCase(HelperConstant.USECASE))
					continue;
				if (meta.getKey().equalsIgnoreCase(HelperConstant.SUBSCRIPTION_LIFE_CYCLE_EVENT))
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
			log.error("error creating renewal job.", e);
			throw new SmException(e);
		}
		return null;
	}

	private SubscriberInfo readEntireSubscriberInfo(String requestId, String subscriberId, List<Meta> metas) {
		ISubscriberRequest iSubscriberRequest = SchedulerContext.getBean(ISubscriberRequest.class);
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
		}
		log.info("Check if response received for read subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;

	}

}

package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
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
import com.ericsson.raso.sef.smart.commons.WalletOfferMapping;
import com.ericsson.raso.sef.smart.commons.WalletOfferMappingHelper;
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
	private String subscribtionId = null;
	private Long schedule;
	private Map<String, Object> metas;
	private Map<String, Long> expiryScheduleMap = null;
	public SubscriptionLifeCycleCommand(String event, String subscriptionId,
			String offerId, String subscriberId, Map<String, Object> metas,
			Long schedule) {
		log.debug("SubscriptionLifeCycleCommand  Constructor  event: " + event
				+ " offerId: " + offerId + " subscriberId: " + subscriberId
				+ " schedule: " + schedule + " metas:" + metas);

		this.event = event;
		this.offerId = offerId;
		this.subscriberId = subscriberId;
		this.metas = metas;
		this.schedule = schedule;
		this.subscribtionId = subscriptionId;
	}

	@Override
	public Void execute() throws SmException {
		try {

			log.debug("Calling SubscriptionLifeCycleCommand execute method event is."
					+ event);
			final ScheduleRequestService mapper = SefCoreServiceResolver
					.getScheduleRequestService();

			ObsoleteCodeDbSequence sequence = mapper
					.scheduledRequestSequence(UUID.randomUUID().toString());
			final long id = sequence.getSeq();
			// Date scheduleTime = new Date(schedule);
			Calendar scheduledTime = Calendar.getInstance();
			// scheduledTime.add(Calendar.SECOND, 20);
			scheduledTime.setTimeInMillis(schedule);
			final ScheduledRequest request = new ScheduledRequest();
			request.setCreated(new Date());
			request.setStatus(ScheduledRequestStatus.SCHEDLUED);
			request.setResourceId(HelperConstant.RESOURCE_NAME);
			request.setId(id);
			if ("NEW_PURCHASE".equals(event)) {
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.NEW_PURCHASE);
			} else if ("RENEWAL".equals(event)) {
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.RENEWAL);

			} else if ("EXPIRY".equals(event)) {

				prepareExpiryNotificationCommand(subscribtionId, offerId,
						subscriberId, metas);
				scheduledTime.add(Calendar.SECOND, -5); // Expiry is scheduled 5
														// seconds before the
														// renewal time that is
														// passed. Need to
														// configured in GLobal
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.EXPIRY);

			} else if ("TERMINATION".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.TERMINATION);
			else if ("PRE_EXPIRY".equals(event))
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.PRE_EXPIRY);
			request.setMsisdn(subscriberId);
			request.setUserId(subscriberId);
			request.setOfferId(offerId);

			if (schedule < 0) {
				log.error("Received -1.. It is an infinite Schedule..Stopping further execution ");
				return null;
			}

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

			request.setScheduleTime(scheduledTime.getTime());

			log.debug("Preparing for Quartz...scheduleTime is "
					+ scheduledTime.getTime());
			String jobId = event + '-' + String.valueOf(id);
			log.debug("jobID: " + jobId);
			request.setJobId(jobId);

			log.debug("Quartz request: " + request);

			JobDetail job = newJob(SubscriptionLifeCycleJob.class)
					.withIdentity(jobId).build();

			log.debug("Quartz Job Created: Key is " + job.getKey());

			log.debug("Quartz Job Created: Trigger Event Key is " + event + '-'
					+ String.valueOf(id) + " scheduleTime=  "
					+ formatter.format(scheduledTime.getTime()));

			SimpleTrigger trigger = (SimpleTrigger) newTrigger()
					.withIdentity(event + '-' + String.valueOf(id))
					.startAt(scheduledTime.getTime()) // some Date
					.forJob(job.getKey()) // identify job with name
					.build();

			log.debug("See whats in this trigger: " + trigger);

			mapper.insertScheduledRequest(request);
			ScheduledRequestMeta requestMeta = null;
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
			log.debug("Checking Schedule Meta before insert: " + requestMeta);
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

	private void prepareExpiryNotificationCommand(String subscriptionId,
			String offerId, String subscriberId, Map<String, Object> metas)
			throws SmException {

		log.debug("Inside prepareExpiryNotificationCommand ");
		if (metas != null) {
			if (metas.containsKey(HelperConstant.RECHARGE)) {
				long expirySchedule = 0;
				String recharge = (String) metas.get(HelperConstant.RECHARGE);

				log.debug("Inside prepareExpiryNotificationCommand recharge metaValue is "
						+ recharge);
				List<String> preExpiryResourceIdList = new ArrayList<String>();
				if (HelperConstant.PREDEFINED.equals(recharge)
						|| HelperConstant.UNLI.equals(recharge)) {
					preExpiryResourceIdList = getOfferIdListForPreDefinedOrUnli(metas);

				} else if (HelperConstant.FLEXI.equals(recharge)) {
					preExpiryResourceIdList = getOfferIdListForFlexi(metas);
				}

				log.debug("preExpiryResourceIdList returned  is "
						+ preExpiryResourceIdList);
				ExpiryNotificationCommand expiryNotificationCommand = null;

				if (expiryScheduleMap == null)
					expiryScheduleMap = getExpiryScheduleMap(metas);

				log.debug("expiryScheduleMap returned  is " + expiryScheduleMap);
				
				boolean csOffer = false;
				
				for (String resourceId : preExpiryResourceIdList) {
					expirySchedule = 0;
					if (expiryScheduleMap.containsKey(resourceId))
						expirySchedule = expiryScheduleMap.get(resourceId);

					log.debug("expirySchedule  is " + expirySchedule);
					
					if (expirySchedule > 0) {
						log.debug(
								"About to fire NotificationPreExpiry Command for resourceId "
										+ resourceId, " expiryTime="
										+ resourceId);
						expiryNotificationCommand = new ExpiryNotificationCommand(
								subscriberId, resourceId, offerId,
								expirySchedule, metas,
								HelperConstant.NOTIFICATION_PRE_EXPIRY);
						expiryNotificationCommand.execute();
						log.debug("NotificationPreExpiry Command Fired Successfully ");
						
						
						expiryNotificationCommand = new ExpiryNotificationCommand(
								subscriberId, resourceId, offerId,
								expirySchedule, metas,
								HelperConstant.NOTIFICATION_ON_EXPIRY);
						expiryNotificationCommand.execute();
						
						log.debug("Notification_Expiry Command Fired Successfully ");
						
						csOffer = true;
					}
				}

		/* Set<String> keySet = expiryScheduleMap.keySet();
				String key = null;
				for (Iterator<String> i = keySet.iterator(); i.hasNext();) {

					key = i.next();
					Long value = expiryScheduleMap.get(key);
					log.debug(
							"About to fire Notification_Expiry Command for resourceId "
									+ key, " expiryTime=" + value);
					expiryNotificationCommand = new ExpiryNotificationCommand(
							subscriberId, key, offerId, value, metas,
							HelperConstant.NOTIFICATION_ON_EXPIRY);
					expiryNotificationCommand.execute();
					log.debug("Notification_Expiry Command Fired Successfully ");
				
				}
*/
			}
		}
	}

	private List<String> getOfferIdListForPreDefinedOrUnli(
			Map<String, Object> metas) {
		log.debug("Inside getOfferIdListForPreDefinedOrUnli");

		List<String> resourceIdList = new ArrayList<String>();
		if (metas.containsKey(HelperConstant.EVENT_NAME)) {
			String eventName = (String) metas.get(HelperConstant.EVENT_NAME);
			String commaSeparatedResources = SefCoreServiceResolver
					.getConfigService().getValue(
							HelperConstant.EVENT_OFFER_SECTION_NAME, eventName);
			log.debug("getOfferIdListForPreDefinedOrUnli eventName = "
					+ eventName + ", commaSeparatedResources= "
					+ commaSeparatedResources);

			if (commaSeparatedResources != null) {
				StringTokenizer strTokenizer = new StringTokenizer(
						commaSeparatedResources, ",");
				while (strTokenizer.hasMoreElements()) {
					String resourceId = (String) strTokenizer.nextElement();
					log.debug("resourceId TOkenized " + resourceId);
					if (matchOfferIdWithAIRRepsonse(resourceId, metas))
						resourceIdList.add(resourceId);
				}
			}

		}
		log.debug("getOfferIdListForPreDefinedOrUnli Ended , returning resourceIdList = "
				+ resourceIdList);

		return resourceIdList;
	}

	private boolean matchOfferIdWithAIRRepsonse(String resourceId,
			Map<String, Object> metas) {
		log.debug("matchOfferIdWithAIRRepsonse Started for resourceID= "
				+ resourceId);

		boolean returnFlag = false;
		Set<String> keySet = metas.keySet();
		String key = null;
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;
			log.debug("matchOfferIdWithAIRRepsonse key= " + key);
			if (key.startsWith(HelperConstant.ACC_AFTER_OFFER)) {
				value = (String) metas.get(key);
				log.debug("matchOfferIdWithAIRRepsonse Matched key= " + key
						+ ",value=" + value);
				if (value != null) {
					int firstCommaIndex = value.indexOf(",");
					log.debug("matchOfferIdWithAIRRepsonse firstCommaIndex= "
							+ firstCommaIndex + " and substring is ="
							+ value.substring(0, firstCommaIndex));
					if (resourceId.equals(value.substring(0, firstCommaIndex))) {
						returnFlag = true;
						break;
					}
				}
			}
		}
		log.debug("matchOfferIdWithAIRRepsonse  Ended returnFlag is "
				+ returnFlag);

		return returnFlag;
	}
	private List<String> getOfferIdListForFlexi(Map<String, Object> metas) {

		log.debug("getOfferIdListForFlexi  Started  ");

		List<String> resourceIdList = new ArrayList<String>();
		if (metas.containsKey(HelperConstant.WALLET_NAME)) {
			String walletName = (String) metas.get(HelperConstant.WALLET_NAME);
			log.debug("getOfferIdListForFlexi  walletName is " + walletName);
			if (walletName != null) {
				WalletOfferMapping offerMapping = WalletOfferMappingHelper
						.getInstance().getOfferMapping(walletName);
				log.debug("WalletOfferMapping  offerMapping is " + offerMapping);
				if (offerMapping != null) {
					String resourceId = offerMapping.getOfferID();
					log.debug("WalletOfferMapping  resourceId is " + resourceId);
					resourceIdList.add(resourceId);
				}
			}
		}
		log.debug("getOfferIdListForFlexi  Ended resourceIdList is   "
				+ resourceIdList);
		return resourceIdList;
	}

	private Map<String, Long> getExpiryScheduleMap(Map<String, Object> metas) {

		log.debug("getExpiryScheduleMap  Started..  ");
		Map<String, Long> expiryResourceScheduleMap = new HashMap<String, Long>();
		Set<String> keySet = metas.keySet();
		String key = null;
		StringTokenizer resourceTokenizer = null;
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			log.debug("getExpiryScheduleMap  key is ..  " + key);
			String value = null;
			if (key.startsWith(HelperConstant.ACC_AFTER_OFFER)) {
				value = (String) metas.get(key);
				log.debug("getExpiryScheduleMap  key= " + key + "  value ="
						+ value);
				resourceTokenizer = new StringTokenizer(value, ",");
				String resourceId = (String) resourceTokenizer.nextElement();
				String endDate = (String) resourceTokenizer.nextElement();
				expiryResourceScheduleMap.put(resourceId,
							Long.parseLong(endDate));
				log.debug("getExpiryScheduleMap  resourceId= " + resourceId
						+ "  endDate =" + endDate);

			}
		}
		log.debug("getExpiryScheduleMap  Ended..returning.  "
				+ expiryResourceScheduleMap);
		return expiryResourceScheduleMap;
	}
}

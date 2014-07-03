package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.quartz.JobDetail;
import org.quartz.JobKey;
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
import com.ericsson.sef.scheduler.ExpiryNotificationJob;
import com.ericsson.sef.scheduler.HelperConstant;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;

/*Expiry Notification Command and Job design are told by Sathya and Navneet,  implemented as per their input on 03-July-2014 2 AM at Mandrin Hotel*/
public class ExpiryNotificationCommand implements Command<Void> {

	private static Logger log = LoggerFactory
			.getLogger(ExpiryNotificationCommand.class);

	private String resourceId;
	private String msisdn;
	private String offerId;
	private long expirySchedule;
	Map<String, Object> metas = null;
	String eventType;

	public ExpiryNotificationCommand(String msisdn, String resourceId,
			String offerId, long expirySchedule, Map<String, Object> metas,
			String eventType) {

		log.debug("Inside ExpiryNotificationCommand constructor msisdn ="
				+ msisdn + " ,resourceId= " + resourceId + " ,offerId ="
				+ offerId + " ,expirySchedule= " + expirySchedule + " ,metas= "
				+ metas + ", eventType =" + eventType);
		this.resourceId = resourceId;
		this.msisdn = msisdn;
		// this.events = events;
		this.offerId = offerId;
		this.expirySchedule = expirySchedule;
		this.metas = metas;
		this.eventType = eventType;
	}

	@Override
	public Void execute() throws SmException {
		try {

			log.debug("Inside ExpiryNotificationCommand execute");

			final ScheduledRequest scheduledRequest = new ScheduledRequest();
			scheduledRequest.setMsisdn(msisdn);
			scheduledRequest.setResourceId(resourceId);
			scheduledRequest.setStatus(ScheduledRequestStatus.SCHEDLUED);
			
			SchedulerService scheduler = SchedulerContext.getSchedulerService();

			Collection<ScheduledRequest> requests = SefCoreServiceResolver
					.getScheduleRequestService().findIdenticalRequests(
							scheduledRequest);
			log.debug("Inside ExpiryNotificationCommand execute identifical ScheduledRequest are "
					+ requests);

			List<JobKey> jobKeys = new ArrayList<JobKey>();
			boolean isJobToBeScheduled = true;
			if (requests != null && requests.size() > 0) {
				for (ScheduledRequest rq : requests) {
					log.debug("Inside expiry Time from dataBase is "
							+ rq.getExpiryTime());
					
					if (rq.getExpiryTime() != null) {
						log.debug("Inside expiry Time from dataBase is "
								+ rq.getExpiryTime().getTime());
						if (rq.getExpiryTime().getTime() > expirySchedule
								&& eventType.equals(rq.getLifeCycleEvent()
										.name())) {
							isJobToBeScheduled = false;
							break;
						}
					}
				}
				if (isJobToBeScheduled) {
					for (ScheduledRequest rq : requests) {
						jobKeys.add(new JobKey(rq.getJobId()));
						log.debug("Inside ExpiryNotificationCommand checking whether the schedule with Job ID"
								+ rq.getJobId() +" can be deleted or not");
						if (rq.getExpiryTime() != null) {
							
							if (rq.getExpiryTime().getTime() > expirySchedule
									&& eventType.equals(rq.getLifeCycleEvent()
											.name())) {
								
								scheduler.deleteJob(rq.getJobId());

								rq.setStatus(ScheduledRequestStatus.REMOVED);
								SefCoreServiceResolver
										.getScheduleRequestService()
										.upadteScheduledRequestStatus(rq);
							
								log.debug("Job Deleted successfully "
										+ rq.getJobId());
								
							}
						}
					}
				}
			}

			//Added only for Testing START
			Calendar cal1 = Calendar.getInstance();
			cal1.add(Calendar.MINUTE,3);
			expirySchedule = cal1.getTimeInMillis();
			//Added only for Testing START
			
			if (isJobToBeScheduled)
			{
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSSZ");
				log.debug("Expiry Scheudle to be called for Event Scheduling is " +formatter.format(new Date(expirySchedule)));
				scheduleEvent(expirySchedule, eventType);
			}
			log.debug("Inside ExpiryNotificationCommand Completed  isJobToBeScheduled "
					+ isJobToBeScheduled);

		} catch (Exception e) {
			log.error(
					"Excpetion in ExpiryNotification Command" + e.getMessage(),
					e);
			throw new SmException(e);
		}
		return null;
	}
	private void scheduleEvent(long scheduleTimeInMillis1, String eventType)
			throws SmException {
		try {
			log.debug("Inside scheduleEvent scheduleTimeInMillis = "
					+ scheduleTimeInMillis1 + "  ,eventType =" + eventType);

			final ScheduleRequestService mapper = SefCoreServiceResolver
					.getScheduleRequestService();
			ObsoleteCodeDbSequence sequence = mapper
					.scheduledRequestSequence(UUID.randomUUID().toString());
			final long id = sequence.getSeq();
			final ScheduledRequest request = new ScheduledRequest();
			request.setCreated(new Date());
			request.setId(id);
			request.setMsisdn(msisdn);
			request.setUserId(msisdn);
			request.setResourceId(resourceId);
			request.setOfferId(offerId);
			request.setStatus(ScheduledRequestStatus.SCHEDLUED);

			request.setExpiryTime(new Date(scheduleTimeInMillis1));
			long preExpiryOffset = 0;
			if (HelperConstant.NOTIFICATION_PRE_EXPIRY.equals(eventType)) {

				preExpiryOffset = Long.parseLong(SefCoreServiceResolver
						.getConfigService().getValue("GLOBAL",
								HelperConstant.PRE_EXPIRY_OFFSET));
				preExpiryOffset = 60000; // To be commented out
				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.NOTIFICATION_PRE_EXPIRY);

			} else if (HelperConstant.NOTIFICATION_ON_EXPIRY.equals(eventType)) {

				request.setLifeCycleEvent(SubscriptionLifeCycleEvent.NOTIFICATION_ON_EXPIRY);
			}

			Date date1 = new Date((scheduleTimeInMillis1 - preExpiryOffset));

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSZ");

			request.setScheduleTime(date1);

			String jobId = eventType + '-' + String.valueOf(id);
			log.debug("Inside ExpiryNotification Command scheduling for = "
					+ formatter.format(date1) + " Job Id is " + jobId);

			request.setJobId(jobId);

			JobDetail job = newJob(ExpiryNotificationJob.class).withIdentity(
					jobId).build();

			SimpleTrigger trigger = (SimpleTrigger) newTrigger()
					.withIdentity(jobId).startAt(date1).forJob(job.getKey())
					.build();

			mapper.insertScheduledRequest(request);

			/*
			 * ScheduledRequestMeta meta = null;
			 * 
			 * log.debug("Inside ExpiryNotification Insering the metats  ");
			 * 
			 * Iterator<Entry<String, Object>> it = metas.entrySet().iterator();
			 * while (it.hasNext()) { Map.Entry<String, Object> pairs =
			 * (Map.Entry<String, Object>) it .next(); meta = new
			 * ScheduledRequestMeta(); meta.setScheduledRequestId(id);
			 * meta.setKey(pairs.getKey()); meta.setValue((String)
			 * pairs.getValue()); mapper.insertScheduledRequestMeta(meta); }
			 */
			SchedulerService scheduler = SchedulerContext.getSchedulerService();
			scheduler.scheduleJob(job, trigger);
			log.debug("Inside ExpiryNotification Job Scheduled Successfully  ");
		} catch (Exception e) {
			log.error("error creating expiry notification job.", e);
			throw new SmException(e);
		}
	}

}

package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.Period;
import com.ericsson.raso.sef.core.db.mapper.ScheduledRequestMapper;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestStatus;
import com.ericsson.raso.sef.core.db.model.ObsoleteCodeDbSequence;
import com.ericsson.raso.sef.core.db.model.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.core.ne.Language;
import com.ericsson.raso.sef.core.ne.NotificationMessage;
import com.ericsson.raso.sef.core.ne.StringUtils;
import com.ericsson.raso.sef.core.ne.SubscriptionNotificationEvent;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.HelperConstant;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.scheduler.ExpiryNotificationJob;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;
/*import com.ericsson.sm.api.subscriber.Subscriber;
import com.ericsson.sm.api.subscriber.SubscriberManagement;
import com.ericsson.sm.api.subscriber.WSException;
import com.ericsson.sm.core.Command;
import com.ericsson.sm.core.Meta;
import com.ericsson.sm.core.Period;
import com.ericsson.sm.core.SmConstants;
import com.ericsson.sm.core.SmException;
import com.ericsson.sm.core.StringUtils;
import com.ericsson.sm.core.config.Language;
import com.ericsson.sm.core.db.mapper.ScheduledRequestMapper;
import com.ericsson.sm.core.db.model.ScheduledRequest;
import com.ericsson.sm.core.db.model.ScheduledRequestMeta;
import com.ericsson.sm.core.db.model.ScheduledRequestStatus;
import com.ericsson.sm.core.db.model.SmSequence;
import com.ericsson.sm.core.db.model.SubscriptionLifeCycleEvent;
import com.ericsson.sm.core.notification.NotificationMessage;
import com.ericsson.sm.core.notification.SubscriptionNotificationEvent;*/
import com.hazelcast.core.ISemaphore;

public class ExpiryNotificationCommand implements Command<Void> {

	private static Logger log = LoggerFactory.getLogger(ExpiryNotificationCommand.class);

	private String resourceId;
	private String msisdn;
	private Collection<SubscriptionNotificationEvent> events;
	private String productId;
	private Date expirydate;

	public ExpiryNotificationCommand(String msisdn, String resourceId, String productId, Date expiryDate,
			Collection<SubscriptionNotificationEvent> events) {
		this.resourceId = resourceId;
		this.msisdn = msisdn;
		this.events = events;
		this.productId = productId;
		this.expirydate = expiryDate;
	}

	@Override
	public Void execute() throws SmException {
		try {
			/*SubscriberManagement subscriberManagement = SchedulerContext.getSubscriberManagement();
			Subscriber subscriber = subscriberManagement.getSubscriberProfile(msisdn, null);
*/			
			Subscriber subscriber = null;
			try{
				String requestId = RequestContextLocalStore.get().getRequestId();
//				List<com.ericsson.sef.bes.api.entities.Meta> metaList = new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
//				metaList.add(new com.ericsson.sef.bes.api.entities.Meta("READ_SUBSCRIBER", "SIMP"));
				SubscriberInfo subscriberInfo = readEntireSubscriberInfo(requestId,
						msisdn, null);

				log.debug("subscriberInfo returned is " +subscriberInfo);
				subscriber = subscriberInfo.getSubscriber();
				if(subscriber == null){
					throw new RuntimeException("User with userID: " + msisdn + " does not exist anymore: ");
				}
				}catch(Exception e1){
					throw new RuntimeException(e1);
				}
			Language language = Language.en;
			if (subscriber.getPrefferedLanguage() != null) {
				language = Language.valueOf(subscriber.getPrefferedLanguage());
			}

			final ScheduledRequest scheduledRequest = new ScheduledRequest();
			scheduledRequest.setMsisdn(msisdn);
			scheduledRequest.setResourceId(resourceId);
			scheduledRequest.setStatus(ScheduledRequestStatus.SCHEDLUED);
			SchedulerService scheduler = SchedulerContext.getSchedulerService();

			Collection<ScheduledRequest> requests = SchedulerContext.getScheduledRequestMapper().findIdenticalRequests(
					scheduledRequest);
			List<JobKey> jobKeys = new ArrayList<JobKey>();

			if (requests != null && requests.size() > 0) {
				for (ScheduledRequest rq : requests) {
					jobKeys.add(new JobKey(rq.getJobId()));
				}
				scheduler.deleteJobs(jobKeys);
				scheduledRequest.setStatus(ScheduledRequestStatus.REMOVED);
				SchedulerContext.getScheduledRequestMapper().upadteScheduledRequestStatus(scheduledRequest);
			}
			
			List<PreparedMessage> preparedMessages = new ArrayList<ExpiryNotificationCommand.PreparedMessage>();

			for (SubscriptionNotificationEvent event : events) {
				List<NotificationMessage> filteredMessages = new ArrayList<NotificationMessage>();
				List<NotificationMessage> messages = event.getMessages();
				for (NotificationMessage message : messages) {
					if (message.getLang() == language) {
						filteredMessages.add(message);
					}
				}
				
				List<Meta> placeholderMetas = new ArrayList<Meta>();
				placeholderMetas.add(new Meta(HelperConstant.RESOURCE_ID, resourceId));
				placeholderMetas.add(new Meta(HelperConstant.PRODUCT_ID, productId));
				placeholderMetas.add(new Meta(HelperConstant.EXPIRY_DATE, expirydate.toString()));
				
				switch (event.getEventType()) {
				case EXPIRY:
					PreparedMessage prep = new PreparedMessage();
					prep.scheduleDate = expirydate;
					prep.event = SubscriptionLifeCycleEvent.EXPIRY;
					preparedMessages.add(prep);
					for (NotificationMessage message : filteredMessages) {
						prep.messages.add(StringUtils.prepareMessage(message.getMessage(), placeholderMetas));
					}
					break;
				case PRE_EXPIRY:
					for (Meta meta : event.getMetas()) {
						if (meta.getKey().equals(SubscriptionLifeCycleEvent.PRE_EXPIRY.name())) {
							Period period = Period.valueOf(meta.getValue());
							PreparedMessage msg = new PreparedMessage();
							msg.event = SubscriptionLifeCycleEvent.PRE_EXPIRY;
							msg.scheduleDate = new Date(expirydate.getTime() - period.getPeriodInMills());
							preparedMessages.add(msg);
							for (NotificationMessage message : filteredMessages) {
								List<Meta> list = new ArrayList<Meta>();
								list.addAll(placeholderMetas);
								list.add(new Meta(HelperConstant.VALIDITY_LEFT, period.toString()));
								msg.messages.add(StringUtils.prepareMessage(message.getMessage(), list));
							}
						}
					}
					break;
				default:
					break;
				}
			}
			
			for (PreparedMessage preparedMessage : preparedMessages) {
				scheduleEvent(preparedMessage.scheduleDate, preparedMessage.messages, preparedMessage.event);
			}
			
		}catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			throw new SmException(e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SmException(e);
		}
		return null;
	}

	private void scheduleEvent(Date scheduleTime, final List<String> messages, SubscriptionLifeCycleEvent event)
			throws SmException {
		try {
			/*UserProfileService userProfileService = SchedulerContext.getUserProfileService();
			final String userId = userProfileService.getUserId(msisdn);*/
			
			Subscriber subscriber = null;
			try{
				String requestId = RequestContextLocalStore.get().getRequestId();
				//List<com.ericsson.sef.bes.api.entities.Meta> metaList = new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
				//metaList.add(new com.ericsson.sef.bes.api.entities.Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
				SubscriberInfo subscriberInfo = readEntireSubscriberInfo(requestId,
						msisdn, null);

				log.debug("subscriberInfo returned is " +subscriberInfo);
				subscriber = subscriberInfo.getSubscriber();
				if(subscriber == null){
					throw new RuntimeException("User with userID: " + msisdn + " does not exist anymore: ");
				}
				}catch(Exception e1){
					throw new RuntimeException(e1);
				}

			final ScheduledRequestMapper mapper = SchedulerContext.getScheduledRequestMapper();
			ObsoleteCodeDbSequence sequence = mapper.scheduledRequestSequence(UUID.randomUUID().toString());
			final long id = sequence.getSeq();
			final ScheduledRequest request = new ScheduledRequest();
			request.setCreated(new DateTime());
			request.setId(id);
			request.setLifeCycleEvent(event);
			request.setMsisdn(msisdn);
			request.setUserId(subscriber.getUserId());
			request.setResourceId(resourceId);
			request.setOfferId(productId);
			request.setStatus(ScheduledRequestStatus.SCHEDLUED);
			request.setScheduleTime(new DateTime(scheduleTime));

			String jobId = event.name() + '-' + String.valueOf(id);
			request.setJobId(jobId);
			JobDetail job = newJob(ExpiryNotificationJob.class).withIdentity(jobId).build();

			Trigger trigger = newTrigger().withIdentity(jobId).startAt(scheduleTime).build();

			mapper.insertScheduledRequest(request);
			int i = 1;
			for (String msg : messages) {
				ScheduledRequestMeta meta = new ScheduledRequestMeta();
				meta.setScheduledRequestId(id);
				meta.setKey(HelperConstant.NOTIFICATION_MESSAGE+ "" + i++);
				meta.setValue(msg);
				mapper.insertScheduledRequestMeta(meta);
			}

			SchedulerService scheduler = SchedulerContext.getSchedulerService();
			scheduler.scheduleJob(job, trigger);
		} catch (Exception e) {
			log.error("error creating expiry notification job.", e);
			throw new SmException(e);
		}
	}
	
	public static class PreparedMessage {
		Date scheduleDate;
		SubscriptionLifeCycleEvent event;
		List<String> messages = new ArrayList<String>();
	}
	
	private  SubscriberInfo readEntireSubscriberInfo(String requestId,
			String subscriberId, List<com.ericsson.sef.bes.api.entities.Meta> metas) {
		//ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		ISubscriberRequest iSubscriberRequest = SchedulerContext.getBean(ISubscriberRequest.class);
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
		}
		log.info("Check if response received for read subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;

	}
}

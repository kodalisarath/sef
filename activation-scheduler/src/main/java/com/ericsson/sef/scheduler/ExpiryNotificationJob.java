/*package com.ericsson.sef.scheduler;

import java.util.Collection;
import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestStatus;
import com.ericsson.raso.sef.core.db.service.DbTransactionService;
import com.ericsson.raso.sef.core.db.service.TransactionCallback;
import com.ericsson.raso.sef.core.smpp.SmppMessage;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.HelperConstant;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;
import com.hazelcast.core.ISemaphore;

public class ExpiryNotificationJob implements Job {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobId = context.getJobDetail().getKey().getName();
		log.info("ExpiryNotificationJob executing job with job ID : " + jobId +" JOb details : "+ context.getJobDetail().getJobDataMap().toString());
		
		final ScheduledRequest scheduledRequest = SchedulerContext.getScheduledRequestMapper().getScheduledRequestByJobId(jobId);
		if (scheduledRequest == null) {
			log.error("ExpiryNotificationJob job is null for the given executing job with job ID: " + jobId);
			throw new RuntimeException("ExpiryNotificationJob job is null for the given executing job with job ID: " + jobId);
		}
		
		if (scheduledRequest.getStatus() != ScheduledRequestStatus.SCHEDLUED) {
			log.error("ExpiryNotification job is not set to scheduled for the given executing job with job ID: " + jobId);
			throw new RuntimeException("ExpiryNotification job is not set to scheduled for the given executing job with job ID: " + jobId);
		}
		
		UserProfileService userProfileService = SchedulerContext.getUserProfileService();
		try {
				throw new RuntimeException("User with user ID 			if(!userProfileService.userProfileExist("userID:" + scheduledRequest.getUserId())) {
does not exist anymore: " + scheduledRequest.getUserId());
			}
		} catch (WSException_Exception e1) {
			throw new RuntimeException(e1);
		}
		
		///Start ReadSubscriber ///
//		try{
//		String requestId = RequestContextLocalStore.get().getRequestId();
//		List<Meta> metaList = new ArrayList<Meta>();
//		metaList.add(new Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
//		SubscriberInfo subscriberInfo = readEntireSubscriberInfo(requestId,
//				scheduledRequest.getMsisdn(), metaList);
//
//		log.debug("subscriberInfo returned is " +subscriberInfo);
//		Subscriber subscriber = subscriberInfo.getSubscriber();
//		if(subscriber == null){
//			throw new RuntimeException("User with userID: " + scheduledRequest.getUserId() + " does not exist anymore: ");
//		}
//		}catch(Exception e1){
//			throw new RuntimeException(e1);
//		}
		//Change the code to 
		try {
			SubscriberInfo subscriberInfo = TransactionEngineHelper.getSubscriberInfo(scheduledRequest.getMsisdn());
			Subscriber subscriber = subscriberInfo.getSubscriber();
			if(subscriber == null){
				throw new RuntimeException("User with userID: " + scheduledRequest.getUserId() + " does not exist anymore: ");
			}
		} catch (SmException e1) {
			log.error("Error in getting subscriber info", e1);
			throw new RuntimeException(e1);
		}
		///ENd ReadSubscriber ///

		Collection<ScheduledRequestMeta> metas = SchedulerContext.getScheduledRequestMapper().getScheduledRequestMetas(
				scheduledRequest.getId());
		
		for (ScheduledRequestMeta m : metas) {
			if(m.getKey().startsWith(HelperConstant.NOTIFICATION_MESSAGE)) {
				SmppMessage message = new SmppMessage();
				message.setDestinationMsisdn(scheduledRequest.getMsisdn());
				message.setMessageBody("0," + m.getValue());
				ProducerTemplate template = SchedulerContext.getCamelContext().createProducerTemplate();
				template.sendBody("activemq:queue:notification", message);
			}
		}
		
		try {
			DbTransactionService transactionService = SchedulerContext.getBean(DbTransactionService.class);
			transactionService.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTranscation() throws SmException {
					scheduledRequest.setStatus(ScheduledRequestStatus.COMPLETED);
					SchedulerContext.getScheduledRequestMapper().upadteScheduledRequestStatus(scheduledRequest);
					return null;
				}
			});
		} catch (SmException e) {
			throw new RuntimeException(e);
		} 
		
	}
	
	private  SubscriberInfo readEntireSubscriberInfo(String requestId,
			String subscriberId, List<Meta> metas) {
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
*/
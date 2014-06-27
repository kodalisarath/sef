package com.ericsson.sef.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.HelperConstant;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;

/*import com.drutt.ws.msdp.userprofile.UserProfileService;
import com.drutt.ws.msdp.userprofile.WSException_Exception;
import com.ericsson.sm.api.subscription.Meta;
import com.ericsson.sm.api.subscription.SubscriptionManagement;
import com.ericsson.sm.api.subscription.WSException;
import com.ericsson.sm.core.SmConstants;
import com.ericsson.sm.core.db.model.ScheduledRequestMeta;
import com.ericsson.sm.core.db.model.ScheduledRequest;
import com.ericsson.sm.core.db.model.SubscriptionLifeCycleEvent;*/

public class OfferRenewalJob implements Job {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobId = context.getJobDetail().getKey().getName();
		log.info("OfferRenewalJob executing job with job ID : " + jobId +" JOb details : "+ context.getJobDetail().getJobDataMap().toString());
		
		ScheduledRequest scheduledRequest = SchedulerContext.getScheduledRequestMapper().getScheduledRequestByJobId(jobId);
		if(scheduledRequest == null) {
			log.error("Renewal job is null for the given executing job with job ID: " + jobId);
			throw new RuntimeException("Renewal job is null for the given executing job with job ID: " + jobId);
		}
		
		/*UserProfileService userProfileService = SchedulerContext.getUserProfileService();
		try {
			if(!userProfileService.userProfileExist("userID:" + scheduledRequest.getUserId())) {
				throw new RuntimeException("User with user ID does not exist anymore: " + scheduledRequest.getUserId());
			}
		} catch (WSException_Exception e1) {
			throw new RuntimeException(e1);
		}*/
		try{
			String requestId = RequestContextLocalStore.get().getRequestId();
			List<Meta> metaList1 = new ArrayList<Meta>();
			metaList1.add(new Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
			SubscriberInfo subscriberInfo = readEntireSubscriberInfo(requestId,
					scheduledRequest.getMsisdn(), metaList1);

			log.debug("subscriberInfo returned is " +subscriberInfo);
			Subscriber subscriber = subscriberInfo.getSubscriber();
			if(subscriber == null){
				throw new RuntimeException("User with userID: " + scheduledRequest.getUserId() + " does not exist anymore: ");
			}
			
		/*
		List<Meta> mList = new ArrayList<Meta>();
		mList.add(new Meta(SmartConstants.SUBSCRIPTION_LIFE_CYCLE_EVENT, SubscriptionLifeCycleEvent.RENEWAL.name()));
		mList.add(new Meta(SmartConstants.PURCHASE_ID, scheduledRequest.getPurchaseId()));
		
		for (ScheduledRequestMeta meta : metas) {
			mList.add(new Meta(meta.getKey(), meta.getValue()));
		}
		
		SubscriptionManagement sm = SchedulerContext.getSubscriptionManagement();
		try {
			sm.purchaseProduct(scheduledRequest.getMsisdn(), scheduledRequest.getOfferId(), mList);
		} catch (WSException e) {
			log.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}*/
		IConfig config = SefCoreServiceResolver.getConfigService();
		String chargeOffer = config.getValue("GLOBAL", "readCustomerInfoChargeOffer");
		List<Meta> metaList2 = new ArrayList<Meta>();
		metaList2.add(new Meta(HelperConstant.REQUEST_ID, requestId));
		metaList2.add(new Meta(HelperConstant.SERVICE_IDENTIFIER, HelperConstant.FLEXIBLE_SERVICE_IDENTIFIER));
		//Below parameters will be tested later.
		metaList2.add(new Meta(Constants.CHANNEL_NAME, "TEST"));
		metaList2.add(new Meta(Constants.EX_DATA1, "TEST"));
		/*metaList.add(new Meta(Constants.EX_DATA1, String.valueOf(request.getMessageId())));
		metaList.add(new Meta(Constants.CHANNEL_NAME, request.getChannel()));
		*/	//subscriptionManagement.purchaseProduct(request.getCustomerId(), chargeOffer, metas);
		PurchaseResponse purchaseResponse = purchase(requestId,chargeOffer,scheduledRequest.getMsisdn(),metaList2);
		if(purchaseResponse == null){
			throw new RuntimeException("Problem Purchasing "+scheduledRequest.getUserId());
		}
		}catch(Exception e1){
			throw new RuntimeException(e1);
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
	
	private PurchaseResponse purchase(String requestId,String packaze, String customerId,	List<Meta> metas )
	{
		ISubscriptionRequest iSubscriptionRequest = SchedulerContext.getBean(ISubscriptionRequest.class);

		PurchaseResponse purchaseResponse = new PurchaseResponse();
		RequestCorrelationStore.put(requestId, purchaseResponse);

		iSubscriptionRequest.purchase(requestId, packaze, customerId, false,
				metas);

		log.info("Invoking purchasebe on tx-engine subscribtion interface");

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		log.info("Check if response received for purchase response");
		PurchaseResponse purRsp = (PurchaseResponse) RequestCorrelationStore
				.remove(requestId);

	return purRsp;
	}
}

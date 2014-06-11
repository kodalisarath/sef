package com.ericsson.raso.sef.smart.subscriber.response;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;
import com.hazelcast.core.ISemaphore;

public class SubscriberResponseHandler implements ISubscriberResponse {
	
	private static final Logger logger = LoggerFactory.getLogger(SubscriberResponseHandler.class);

	@Override
	public void readSubscriber(String requestCorrelator, TransactionStatus fault, Subscriber subscriber) {

		logger.debug("readSubscriber CorrelationID: " + requestCorrelator);

		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.get(requestCorrelator);

		if (fault != null) {
			subscriberInfo.setStatus(fault);
			this.triggerResponse(requestCorrelator, subscriberInfo);
			return;
		} 
		
		if (subscriber == null) {
			subscriberInfo.setStatus(new TransactionStatus("txe", 504, "Invalid Account - Subscriber canot be found!!"));
			this.triggerResponse(requestCorrelator, subscriberInfo);
			logger.debug("Subscriber is null: " + requestCorrelator);
			return;
		}
		
		else if (subscriber.getMsisdn() == null || 
				subscriber.getUserId() == null|| 
				subscriber.getCustomerId() == null|| 
				subscriber.getContractId() == null || 
				subscriber.getContractState() == null ||
				subscriber.getMetas() == null ) {
			
			subscriberInfo.setStatus(new TransactionStatus("txe", 2002, "Invalid Account - Subscriber entity seems to be corrupt or badly managed!!"));
			this.triggerResponse(requestCorrelator, subscriberInfo);
			return;
		}

		try {
			subscriberInfo.setMsisdn(subscriber.getMsisdn());
			subscriberInfo.setLocalState(ContractState.apiValue(subscriber.getContractState()));
			Map<String, String> subscriberMetas = subscriber.getMetas();

			//get the subscriber status from back end
			String activationStatus = subscriberMetas.get(Constants.READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG);
			logger.debug("Subscriber activationStatus: " +  activationStatus);
			boolean isActive = false;
			if(activationStatus != null) {
				logger.warn("No activation status flag received from the back end");
				isActive = Boolean.parseBoolean(activationStatus);
			}

			if(isActive) {
				subscriberInfo.setRemoteState(ContractState.ACTIVE);
			} else {
				subscriberInfo.setRemoteState(ContractState.PREACTIVE);
			}


			//check if subscriber in recycle state

			boolean recycleStat = false;

			for(int i=0; i<32; i++) {
				String recycleStatus = subscriberMetas.get(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID+"." + i);
				if(recycleStatus != null && recycleStatus.equalsIgnoreCase("2")) {
					String recycleActivationStatus = subscriberMetas.get(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG +"." + i);
					logger.debug("Subscriber Recycle status: " + recycleActivationStatus);
					recycleStat = Boolean.parseBoolean(recycleActivationStatus);
				}
			}

			if(recycleStat) {
				subscriberInfo.setRemoteState(ContractState.RECYCLED);
			}

			//Check taggin status
			if(subscriberInfo.getRemoteState() == ContractState.ACTIVE) {

			}

		} catch(Exception e) {
			logger.error("Processing error while handling ReadSubscriberResponse. Cause: " + e.getMessage(), e);
			subscriberInfo.setStatus(new TransactionStatus("txe", 11614, "Processing error while handling ReadSubscriberResponse. Cause: " + e.getMessage()));
		}
		this.triggerResponse(requestCorrelator, subscriberInfo);
	
		
	}
	
	private void triggerResponse(String requestId, SubscriberInfo subscriber) {
		SubscriberResponseStore.put(requestId, subscriber);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		semaphore.release();
		
	}

	@Override
	public void readSubscriberMeta(String requestCorrelator,
			TransactionStatus fault, String subscriberId, List<Meta> metaNames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createSubscriber(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		SubscriberInfo subInfo = (SubscriberInfo) SubscriberResponseStore.get(requestCorrelator);
		subInfo.setStatus(fault);
        SubscriberResponseStore.put(requestCorrelator, subInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public void updateSubscriber(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		SubscriberInfo subInfo = (SubscriberInfo) SubscriberResponseStore.get(requestCorrelator);
		subInfo.setStatus(fault);
        SubscriberResponseStore.put(requestCorrelator, subInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public void deleteSubscriber(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		SubscriberInfo subInfo = (SubscriberInfo) SubscriberResponseStore.get(requestCorrelator);
		subInfo.setStatus(fault);
        SubscriberResponseStore.put(requestCorrelator, subInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
		
	}

	@Override
	public void handleLifeCycle(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		SubscriberInfo subInfo = (SubscriberInfo) SubscriberResponseStore.get(requestCorrelator);
		subInfo.setStatus(fault);
        SubscriberResponseStore.put(requestCorrelator, subInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
		
	}

}

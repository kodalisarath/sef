package com.ericsson.raso.sef.smart.subscriber.response;

import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;

public class SubscriberResponseHandler implements ISubscriberResponse {

	@Override
	public void readSubscriber(String requestCorrelator,
			TransactionStatus fault, Subscriber subscriber) {
		
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.get(requestCorrelator);
		
		synchronized (subscriberInfo) {
			
		subscriberInfo.setMsisdn(subscriber.getMsisdn());
		subscriberInfo.setLocalState(ContractState.apiValue(subscriber.getContractState()));
		Map<String, String> subscriberMetas = subscriber.getMetas();
		
		//get the subscriber status from back end
		String activationStatus = subscriberMetas.get(Constants.READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG);
		boolean isActive = Boolean.parseBoolean(activationStatus);
		if(isActive) {
			subscriberInfo.setRemoteState(ContractState.ACTIVE);
		} else {
			subscriberInfo.setRemoteState(ContractState.PREACTIVE);
		}
		
		//check if subscriber in recycle state
		
		boolean recycleStat = false;
		
		for(int i=0; i<32; i++) {
			String recycleStatus = subscriberMetas.get(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID+"." + i);
			if(recycleStatus.equalsIgnoreCase("2")) {
				String recycleActivationStatus = subscriberMetas.get(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG +"." + i);
				recycleStat = Boolean.parseBoolean(recycleActivationStatus);
			}
		}
		
		if(recycleStat) {
			subscriberInfo.setRemoteState(ContractState.RECYCLED);
		}
		
		//Check taggin status
		if(subscriberInfo.getRemoteState() == ContractState.ACTIVE) {
			
		}
		 subscriberInfo.notify();
		}
	}

	@Override
	public void readSubscriberMeta(String requestCorrelator,
			TransactionStatus fault, String subscriberId, List<Meta> metaNames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createSubscriber(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSubscriber(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSubscriber(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleLifeCycle(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		// TODO Auto-generated method stub
		
	}

}

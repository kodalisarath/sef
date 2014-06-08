package com.ericsson.raso.sef.smart.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.commons.SmartServiceHelper;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;


public class SubscriberValidationProcessor {
	private static Logger log = LoggerFactory.getLogger(SubscriberValidationProcessor.class);

	
	public static void process(String msisdn) throws SmException {
		
		SubscriberInfo subscriberInfo =  SmartServiceHelper.getAndRefreshSubscriber(msisdn);
		
		
		
		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState().name().equals(ContractState.RECYCLED.name()))) {
			log.error("Subscriber:" + msisdn + " is in Recycle State.");
			throw new SmException(ErrorCode.invalidCustomerLifecycleStateRecycle);
		}
		
		if(subscriberInfo.isLocked()){
			log.error("Subscriber:" + msisdn + " is Locked or Barred.");
			throw new SmException(ErrorCode.subscriberLocked);
		}
}
}

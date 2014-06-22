package com.ericsson.raso.sef.cg.engine.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.Operation;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.common.CGEngineServiceHelper;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;

public class SubscriberValidationProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();
	//	SmartCommonService smartCommonService = CgEngineContext.getSmartCommonService();
		  

		SubscriberInfo  subscriberInfo = null;
		try {
			subscriberInfo =CGEngineServiceHelper.getSubscriberInfo(request.getMsisdn());
		} catch (SmException e) {
			if(e.getStatusCode().getCode() == 102) {
				throw new SmException(ResponseCode.SUBSCRIBER_NOT_FOUND);
			} else throw e;
		}
		
		Operation operation = request.getOperation();
		
		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.GRACE)) {
			log.error("Subscriber:" + request.getMsisdn() + " is in Grace State.");
			throw new SmException(ResponseCode.SUBSCRIBER_GRACE_NOT_ALLOWED);
		}

		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.RECYCLED)) {
			log.error("Subscriber:" + request.getMsisdn() + " is in RECYCLED State.");
			throw new SmException(ResponseCode.SUBSCRIBER_RECYCLE_NOT_ALLOWED);
		}
		
		if(subscriberInfo.isLocked()){
			log.error("Subscriber:" + request.getMsisdn() + " is in Barred/Locked State.");
			throw new SmException(ResponseCode.SUBSCRIBER_LOCKED);
		}
		
		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.PREACTIVE)) {
			if(operation != Operation.MMS_CHARGING) {
				log.error("Subscriber:" + request.getMsisdn() + " is in Preactive State.");
				throw new SmException(ResponseCode.SUBSCRIBER_PREACTIVE_NOT_ALLOWED);
			}
		}
	}


}

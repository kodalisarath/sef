package com.ericsson.raso.sef.cg.engine.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.CgConstants;
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
		String messageId = (String)exchange.getIn().getHeader(CgConstants.messageId);
		String msisdn = request.getMsisdn();
		
		log.info("Start validating subsciber for message Id "+messageId + " msisdn is :"+ msisdn);
		
		if ( msisdn == null || msisdn.length() == 0){
			log.error("MSISND is null, can not perform validation. throw exception ");
			throw new SmException(ResponseCode.SUBSCRIBER_NOT_FOUND);
		}
		SubscriberInfo  subscriberInfo = null;
		try {
			subscriberInfo =CGEngineServiceHelper.getSubscriberInfo(msisdn);
			
			log.info(" CGEngine SubscriberValidationProcessor subscriberInfo for message Id "+messageId +" &  msisdn "+msisdn +" subscriberInfor is "+subscriberInfo);
			
		} catch (SmException e) {
			log.error("Exception in ChargingGatewayEngine.SubscriberValidationProcessor "+e.getMessage(), e);
			if(e.getStatusCode().getCode() == 102) {
				throw new SmException(ResponseCode.SUBSCRIBER_NOT_FOUND);
			} else throw e;
		}
		
		log.debug("SubscriberInfo in charging gateway engine: "+subscriberInfo  +" & msisdn: "+msisdn);
		if ( subscriberInfo != null){
			log.debug(String.format("subscirberInfo.getLocalState = %s, subscirberInfo.getLocalState =%s", subscriberInfo.getLocalState(), subscriberInfo.isLocked()));
		}
		else
		{
			log.error("Exception in ChargingGatewayEngine.SubscriberValidationProcessor subscriberInfo is null");
			throw new SmException(ResponseCode.SUBSCRIBER_NOT_FOUND);
		}
		  
		Operation operation = request.getOperation();
		
		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.GRACE)) {
			log.error("Subscriber:" + msisdn + " is in Grace State.");
			throw new SmException(ResponseCode.SUBSCRIBER_GRACE_NOT_ALLOWED);
		}
  
		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.RECYCLED)) {
			log.error("Subscriber:" + msisdn + " is in RECYCLED State.");
			throw new SmException(ResponseCode.SUBSCRIBER_RECYCLE_NOT_ALLOWED);
		}
		
		if(subscriberInfo.isLocked()){
			log.error("Subscriber:" + msisdn + " is in Barred/Locked State.");
			throw new SmException(ResponseCode.SUBSCRIBER_LOCKED);
		}
		
		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.PREACTIVE)) {
			if(operation != Operation.MMS_CHARGING) {
				log.error("Subscriber:" + msisdn + " is in Preactive State.");
				throw new SmException(ResponseCode.SUBSCRIBER_PREACTIVE_NOT_ALLOWED);
			}
		}
		
		String metaKey = "Tagging";
		
		if(subscriberInfo.getMetas() !=null && subscriberInfo.getMetas().containsKey(metaKey) && subscriberInfo.getMetas().get(metaKey) !="0")
		{
			log.error("Subscriber:" + msisdn + " does not have Tagging Meta.");
			throw new SmException(ResponseCode.SUBSCRIBER_LOCKED);
		}
		
	}


}

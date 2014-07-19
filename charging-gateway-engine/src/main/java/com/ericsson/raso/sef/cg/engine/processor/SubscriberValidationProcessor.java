package com.ericsson.raso.sef.cg.engine.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.CgConstants;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.common.CGEngineServiceHelper;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.cg.model.Operation;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;

public class SubscriberValidationProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER = "READ_SUBSCRIBER_OFFER_INFO";
	
	

	public void process(Exchange exchange) throws Exception {
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();
	//	SmartCommonService smartCommonService = CgEngineContext.getSmartCommonService();
		String messageId = (String)exchange.getIn().getHeader(CgConstants.messageId);
		String msisdn = request.getMsisdn();
		
		log.info("	Start validating subsciber for message Id "+messageId + " msisdn is :"+ msisdn);
		
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
				log.error("TRYING TO GET SUBSCRIBER AND NOT FOUND" + msisdn);
				throw new SmException(ResponseCode.SUBSCRIBER_NOT_FOUND);
			} else throw e;
		}
		
		//log.error("SubscriberInfo in charging gateway engine: "+subscriberInfo  +" & msisdn: "+msisdn);
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() >0){
			log.error("Inside the if condition for status check" + msisdn);
			throw new SmException(ResponseCode.SUBSCRIBER_NOT_FOUND);
		}
		  
		Operation operation = request.getOperation();
		
//		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.GRACE)) {
//			log.error("Subscriber:" + msisdn + " is in Grace State.");
//			throw new SmException(ResponseCode.SUBSCRIBER_GRACE_NOT_ALLOWED);
//		}
//  
//		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.RECYCLED)) {
//			log.error("Subscriber:" + msisdn + " is in RECYCLED State.");
//			throw new SmException(ResponseCode.SUBSCRIBER_RECYCLE_NOT_ALLOWED);
//		}
//		if (subscriberInfo.getLocalState() == null || (subscriberInfo.getLocalState() == ContractState.PREACTIVE)) {
//			if(operation != Operation.MMS_CHARGING) {
//				log.error("Subscriber:" + msisdn + " is in Preactive State.");
//				throw new SmException(ResponseCode.SUBSCRIBER_PREACTIVE_NOT_ALLOWED);
//			}
//		}
//		if(subscriberInfo.isLocked()){
//			log.error("Subscriber:" + msisdn + " is in Barred/Locked State.");
//			throw new SmException(ResponseCode.SUBSCRIBER_LOCKED);
//		}
//		
//		
//		
//		String metaKey = "Tagging";
//		
//		if(subscriberInfo.getMetas() !=null && subscriberInfo.getMetas().containsKey(metaKey) && subscriberInfo.getMetas().get(metaKey) !="0")
//		{
//			log.error("Subscriber:" + msisdn + " does not have Tagging Meta.");
//			throw new SmException(ResponseCode.SUBSCRIBER_LOCKED);
//		}
		
		
		 String activeStatusCS = subscriberInfo.getSubscriber().getMetas().get("READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG");
		 log.debug("STATUS IN CS - "+ activeStatusCS);
	    
	     int usecase=0;
	     log.info("Failfast if pre-active");
	     if (activeStatusCS.equalsIgnoreCase("false"))
	     {
	    	 usecase=1;
	    	 throw new SmException(ResponseCode.SUBSCRIBER_PREACTIVE_NOT_ALLOWED);
	     }
		
		Map<String, String> subscriberMetas = subscriberInfo.getSubscriber().getMetas();
		
		OfferInfo oInfo = null;
		Map<String, OfferInfo> subscriberOffers = new HashMap<String, SubscriberValidationProcessor.OfferInfo>(); 
		boolean IsGrace = false; long graceExpiry = 0;
		for (String key: subscriberMetas.keySet()) {
			log.debug("FLEXI:: processing meta:" + key + "=" + subscriberMetas.get(key));
			if (key.startsWith(READ_SUBSCRIBER_OFFER_INFO_OFFER)) {
				log.debug("FLEXI:: OFFER_ID...." + subscriberMetas.get(key));
				String offerForm = subscriberMetas.get(key);
				
				String offerParts[] = offerForm.split(",");
				String offerId = offerParts[0];
				String start = offerParts[1];
				if (start.equals("null"))
					start = offerParts[2];
				String expiry = offerParts[3];
				if (expiry.equals("null"))
					expiry = offerParts[4];
				
				oInfo = new OfferInfo(offerId, Long.parseLong(expiry), Long.parseLong(start), null, null); 
				subscriberOffers.put(offerId, oInfo);
				log.debug("FLEXI:: OFFER_INFO: " + oInfo);

				if (oInfo.offerID.equals("2")) {
					log.debug("FLEXI:: CUSTOMER IN GRACE!!!");
					usecase=2;
					throw new SmException(ResponseCode.SUBSCRIBER_GRACE_NOT_ALLOWED);
				}
				if (oInfo.offerID.equals("4")) {
					log.debug("FLEXI:: CUSTOMER IN RECYCLE!!!");
					usecase=3;
					throw new SmException(ResponseCode.SUBSCRIBER_RECYCLE_NOT_ALLOWED);
				}
			}
		}
		
		String tagging = subscriberMetas.get("Tagging");
		if (tagging == null)
			tagging = subscriberMetas.get("c_TaggingStatus");
		log.debug("SUBSCRIBER TAGGING IN SMART FORMAT: "  + tagging);
		if (Integer.parseInt(tagging)>=1 && Integer.parseInt(tagging) <=7)
		{
			throw new SmException(ResponseCode.SUBSCRIBER_LOCKED);
		}
			
	}

	class OfferInfo {
		private String offerID;
		private long offerExpiry;
		private long offerStart;
		private String daID;
		private String walletName;
		
		public OfferInfo() {}
		
		public OfferInfo(String offerID, long offerExpiry, long offerStart, String daID, String walletName) {
			super();
			this.offerID = offerID;
			this.offerExpiry = offerExpiry;
			this.offerStart = offerStart;
			this.daID = daID;
			this.walletName = walletName;
		}


		@Override
		public String toString() {
			return "OfferInfo [offerID=" + offerID + ", offerExpiry=" + offerExpiry + ", daID=" + daID + ", walletName=" + walletName + "]";
		}
		
		
	}

}
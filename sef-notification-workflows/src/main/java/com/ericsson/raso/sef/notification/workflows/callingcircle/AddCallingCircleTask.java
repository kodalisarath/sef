package com.ericsson.raso.sef.notification.workflows.callingcircle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vasclient.wsdl.VASClientSEI;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.Task;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.smpp.SmppMessage;
import com.ericsson.raso.sef.notification.workflows.CallingCircleEdrProcessor;
import com.ericsson.raso.sef.notification.workflows.NotificationContext;
import com.ericsson.raso.sef.notification.workflows.ResponseCode;
import com.ericsson.raso.sef.notification.workflows.common.WorkflowEngineServiceHelper;
import com.ericsson.raso.sef.notification.workflows.promo.Promo;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
//import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
//import com.ericsson.sef.scheduler.common.TransactionEngineHelper;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;

public class AddCallingCircleTask implements Task<Void> {

	private static Logger log = LoggerFactory.getLogger(AddCallingCircleTask.class);

	private String msisdn;
	private List<Meta> metas;
	private String productId;
	private String bparty;

	public AddCallingCircleTask(List<Meta> metas) {
		this.metas = metas;
	}

	public Void execute() throws SmException {
		
		for (Meta meta : metas) {
			if (meta.getKey().equalsIgnoreCase(CallingCircleConstants.AP)) {
				msisdn = meta.getValue();
			} else if (meta.getKey().equalsIgnoreCase(CallingCircleConstants.circleID)) {
				productId = meta.getValue();
			} else if (meta.getKey().equalsIgnoreCase(CallingCircleConstants.BP)) {
				bparty = meta.getValue();
			}
		}

		CallingCircleEdrProcessor circleEdrProcessor = new CallingCircleEdrProcessor();

		
		//SmartCommonService smartCommonService = NotificationContext.getBean(SmartCommonService.class);
//		Subscriber  subscriber = null;
//		try {
//			subscriber = WorkflowEngineServiceHelper.getSubscriber( msisdn );
//		} catch (SmException e) {
//			circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(msisdn, bparty, productId, "FAILED", "SUBSCRIBER  " + msisdn + " NOT FOUND"));
//			sendInvalidCallAttemptMessage("800120130000", msisdn);
//			log.error("Subscriber:" + msisdn + " Not found.");
//			if (e.getStatusCode().getCode() == 102) {
//				throw new SmException(ResponseCode.SUBSCRIBER_NOT_FOUND);
//			} else
//				throw e;
//		}
/**
 *		After discussing with Sean Kumar, Sathya, on 25/6/2014
 *		Workflow does not need to perform validation
 * 
 */
//		if (subscriber.getContractState() == null || (subscriber.getContractState() == ContractState.RECYCLED)
//				|| subscriber.getContractState() == ContractState.PREACTIVE) {
//			circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(msisdn, bparty, productId, "FAILED", "SUBSCRIBER  " + msisdn
//					+ " is in RECYCLED/PREACTIVE State."));
//			sendInvalidCallAttemptMessage("800120120000", msisdn);
//			log.error("Subscriber:" + msisdn + " is in RECYCLED/PREACTIVE State.");
//			throw new SmException(ErrorCode.invalidRequest);
//		}
//
//		if (subscriber.isLocked()) {
//			sendInvalidCallAttemptMessage("800101020000", msisdn);
//			circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(msisdn, bparty, productId, "FAILED", "SUBSCRIBER  " + msisdn
//					+ " is in Barred/Locked State"));
//			log.error("Subscriber:" + msisdn + " is in Barred/Locked State.");
//			throw new SmException(ErrorCode.invalidRequest);
//		}

//		subscriber = null;
//		try {
//			//subscriberInfo = smartCommonService.refreshSubscriberState(bparty);
//			//subscriberInfo = ServiceHelper.getAndRefreshSubscriber( bparty );
//			subscriber = WorkflowEngineServiceHelper.getSubscriber(bparty);
//		} catch (SmException e) {
//			if (e.getStatusCode().getCode() == 102) {
//				sendInvalidCallAttemptMessage("800120130000", msisdn);
//				circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(msisdn, bparty, productId, "FAILED", "SUBSCRIBER  " + bparty + " NOT FOUND"));
//				log.error("Subscriber:" + bparty + " NOT FOUND.");
//				throw new SmException(ResponseCode.SUBSCRIBER_NOT_FOUND);
//
//			} else
//				throw e;
//		}

//		if (subscriber.getContractState() == null || (subscriber.getContractState() == ContractState.RECYCLED)
//				|| subscriber.getContractState() == ContractState.PREACTIVE) {
//			sendInvalidCallAttemptMessage("800120120000", msisdn);
//			log.error("Subscriber:" + bparty + " is in RECYCLED/PREACTIVE State.");
//			circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(msisdn, bparty, productId, "FAILED", "Subscriber:" + bparty
//					+ " is in RECYCLED/PREACTIVE State."));
//			throw new SmException(ErrorCode.invalidRequest);
//		}
//
//		if (subscriber.isLocked()) {
//			sendInvalidCallAttemptMessage("800101020000", msisdn);
//			log.error("Subscriber:" + bparty + " is in Barred State.");
//			circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(msisdn, bparty, productId, "FAILED", "Subscriber:" + bparty + " is in Barred State."));
//			throw new SmException(ErrorCode.invalidRequest);
//		}

//		Promo promo = CallingCircleUtil.getCallingCirclePromo(productId);
//		Collection<CallingCircle> callingCircles=null;
//		try {
//			CallingCircleService callingCircleService = NotificationContext.getBean(CallingCircleService.class);
//			callingCircleService.addCircle(productId, msisdn, bparty);
//		} catch (SmException e) {
//			log.error(e.getMessage(),e);
//			circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(msisdn, bparty, productId, "FAILED", e.getMessage()));
//			throw e;
//		}
		
//		if (callingCircles != null) {
//			List<String> msisdns = new ArrayList<String>();
//					
//			for (CallingCircle callingCircle : callingCircles) {
//				if (!msisdns.contains(callingCircle.getApartyMsisdn())) {
//					msisdns.add(callingCircle.getApartyMsisdn());
//				}
//				if (!msisdns.contains(callingCircle.getBpartyMsisdn())) {
//					msisdns.add(callingCircle.getBpartyMsisdn());
//				}
//			}
//
//			for (CallingCircle callingCircle : callingCircles) {
//				circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(callingCircle,"ADD"));
//			}
//			sendEnrollmentMessage(msisdns, promo);
//
//			emlppApi(bparty, productId);
//			if (promo.isOpenCloudRegistration()) {
//				openCloudApi(bparty, productId);
//			}
//		}
		List<com.ericsson.sef.bes.api.entities.Meta> newMetaList = new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
		
		for (Meta meta : metas) {
			com.ericsson.sef.bes.api.entities.Meta newMeta = new com.ericsson.sef.bes.api.entities.Meta();
			newMeta.setKey(meta.getKey());
			if ( meta.getKey().equalsIgnoreCase(CallingCircleConstants.BP)
					|| meta.getKey().equalsIgnoreCase(CallingCircleConstants.AP)){
				newMeta.setValue(appendCountryCode(meta.getValue()));
			} else {
				newMeta.setValue(meta.getValue());
			}
			
			
			newMetaList.add(newMeta);
		}
		com.ericsson.sef.bes.api.entities.Meta workflowMeta = new com.ericsson.sef.bes.api.entities.Meta();
		workflowMeta.setKey("CallingCircleWorkFlow");
		workflowMeta.setValue("true");
		
		com.ericsson.sef.bes.api.entities.Meta subscriberMeta = new com.ericsson.sef.bes.api.entities.Meta();
		subscriberMeta.setKey("SUBSCRIBER_ID");
		subscriberMeta.setValue(appendCountryCode(msisdn));
		
		com.ericsson.sef.bes.api.entities.Meta msisdnMeta = new com.ericsson.sef.bes.api.entities.Meta();
		msisdnMeta.setKey("msisdn");
		msisdnMeta.setValue(appendCountryCode(msisdn));
		
		com.ericsson.sef.bes.api.entities.Meta bpartyMeta = new com.ericsson.sef.bes.api.entities.Meta();
		bpartyMeta.setKey("B-Party");
		bpartyMeta.setValue(appendCountryCode(bparty));
		
		newMetaList.add(workflowMeta);
		newMetaList.add(subscriberMeta);
		newMetaList.add(msisdnMeta);
		newMetaList.add(bpartyMeta);

		
		PurchaseResponse response = TransactionEngineHelper.purchase(productId, appendCountryCode(msisdn), newMetaList);
		
		return null;
	}
	
	private final static String appendCountryCode (String msisdn){
		if ( msisdn.startsWith("0")){
			return msisdn.replaceFirst("0", "63");
		} else if (msisdn.startsWith("63")) {
			return msisdn;
		} else {
			return "63"+msisdn;
		}
	}

//	public void emlppApi(String bparty, String productId) {
//		log.debug(String.format("Query EmlppApi for bparty %s, productId %s.", bparty, productId));
//		try {
//			int ser1 = (int) Math.floor(Math.random() * 900000000) + 1000000000;
//			int ser2 = (int) Math.floor(Math.random() * 900000000) + 1000000000;
//			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//			String emlppString = ser1 + "" + ser2 + ":" + df.format(new Date()) + ":" + "GSFA:G_ULC1::" + bparty + "::38:::::::::::::::::::::::;";
//			VASClientSEI vasClient = NotificationContext.getBean(VASClientSEI.class);
//			String emlResponse = vasClient.sendToSPS(emlppString);
//			log.info(emlResponse);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
//	}

//	public void openCloudApi(String bparty, String productId) {
//		log.error(String.format("Open Cloud API is not implemented."));
//		try {
//			SmartUnliSmsAdmin openCLoudSms = NotificationContext.getBean(SmartUnliSmsAdmin.class);
//			CreateUpdateSubscriberRequest request = new CreateUpdateSubscriberRequest();
//			request.setTxnId(String.valueOf((int) Math.floor(Math.random() * 900000000) + 1000000000));
//			request.setMsisdn(bparty);
//			request.setPackageName(productId);
//			request.setInGTAddress(getInGTAddress());
//			request.setExpiryDate(String.valueOf(new Date().getTime()));
//			openCLoudSms.createUpdateSubscriber(request);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
//	}

//	public String getInGTAddress() {
//		IConfig config = NotificationContext.getBean(IConfig.class);
//		String property = config.getValue("GLOBAL", "InGTAddress");
//		return property;
//	}

//	private void sendEnrollmentMessage(Collection<String> recipientList, Promo promo) {
//		StringBuilder circles = new StringBuilder();
//
//		for (String str : recipientList) {
//			if (circles.length() == 0) {
//				circles.append(str);
//			} else {
//				circles.append("{#}" + str);
//			}
//		}
//
//		int ind = circles.lastIndexOf("{#}");
//		circles = circles.replace(ind, ind + 3, " & ");
//		
//		for (String recipient : recipientList) {
//			SmppMessage message = new SmppMessage();
//			message.setDestinationMsisdn(recipient);
//			message.setMessageBody(promo.getSuccessEventId()+",circle-numbers:" + circles.toString() + ",promo:" + promo.getAssociatedPromo());
//			ProducerTemplate template = NotificationContext.getCamelContext().createProducerTemplate();
//			template.sendBody("activemq:queue:notification", message);
//		}
//	}
//
//	private void sendInvalidCallAttemptMessage(String event, String recipient) {
//		SmppMessage message = new SmppMessage();
//		message.setDestinationMsisdn(recipient);
//		message.setMessageBody(event + "," + "x:X");
//		ProducerTemplate template = NotificationContext.getCamelContext().createProducerTemplate();
//		template.sendBody("activemq:queue:notification", message);
//
//	}
}
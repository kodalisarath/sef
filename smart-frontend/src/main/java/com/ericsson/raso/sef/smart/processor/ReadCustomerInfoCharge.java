package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.AccountInfo;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.commons.SmartModel;
import com.ericsson.raso.sef.smart.commons.SmartServiceHelper;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.BalanceAdjustmentRequest;
import com.ericsson.raso.sef.smart.usecase.ReadCustomerInfoChargeRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class ReadCustomerInfoCharge implements Processor {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final Logger logger = LoggerFactory.getLogger(BalanceAdjustment.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		ReadCustomerInfoChargeRequest request = (ReadCustomerInfoChargeRequest) exchange.getIn().getBody();
		
		 logger.info("Customer Info Charge: process()");
	     String requestId = RequestContextLocalStore.get().getRequestId();
	     List<Meta> metas = new ArrayList<Meta>();
	     logger.info("Collecting SOAP parameters");

	     List<Meta> workflowMetas= new ArrayList<Meta>();
	     workflowMetas.add(new Meta("msisdn", String.valueOf(request.getCustomerId())));
	     workflowMetas.add(new Meta("AccessKey", String.valueOf(request.getAccessKey())));
	     workflowMetas.add(new Meta("Channel", String.valueOf(request.getChannel())));
	     workflowMetas.add(new Meta("MessageId",String.valueOf(request.getMessageId())));

	     List<Meta> metaSubscriber=new ArrayList<Meta>();
	     workflowMetas.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));
	     workflowMetas.add(new Meta("READ_SUBSCRIBER","CUSTOMER_INFO_CHARGE"));
	     
	     logger.info("Collected SOAP parameters");
	     logger.info("Going for Customer Info Charge Call");
	     logger.info("Before read subscriber call");
		
	     SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metaSubscriber);
	     
	     logger.info("subscriber call done");
		 if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0){
			logger.debug("Inside the if condition for status check");
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		 }
         logger.info("Recieved a SubscriberInfo Object and it is not null");
		 logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());
	     
		exchange.getOut().setBody(readAccountInfo(request.getCustomerId(),request.isTransactional()));

	}


	private SubscriberInfo readSubscriber(String requestId, String customerId,
			List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, customerId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		logger.debug("Hi HERE I AM::: Result " + subscriberInfo.getStatus() );
		return subscriberInfo;
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
	
	
	private CommandResponseData readAccountInfo(String msisdn,boolean isTransactional) throws SmException {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);


		OperationResult operationResult = new OperationResult();

		if(isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}

		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("CustomerInfoCharge");
		operationResult.getOperation().add(operation);
		ParameterList parameterList = new ParameterList();
		operation.setParameterList(parameterList);
		
		
		StringParameter accounts = new StringParameter();
		accounts.setName("Accounts");
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(accounts);
		
		
		StringParameter subscriptions = new StringParameter();
		subscriptions.setName("Subscriptions");
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(subscriptions);
		
		return responseData;
	}
}
	

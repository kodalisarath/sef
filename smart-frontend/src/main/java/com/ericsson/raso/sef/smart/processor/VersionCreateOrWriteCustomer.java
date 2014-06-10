package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.VersionCreateOrWriteCustomerRequest;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class VersionCreateOrWriteCustomer implements Processor {

	private static final Logger logger = LoggerFactory
			.getLogger(VersionCreateOrWriteCustomer.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		try {
			// get the method signature of web service [Standard]
			logger.debug("Getting exchange body....");
			VersionCreateOrWriteCustomerRequest versionCreateOrWriteCustomerRequest = (VersionCreateOrWriteCustomerRequest) exchange
					.getIn().getBody();
			logger.debug("Got it!");
			
			List<com.ericsson.sef.bes.api.entities.Meta> metas = new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("customerId", versionCreateOrWriteCustomerRequest.getCustomerId()));
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("category", versionCreateOrWriteCustomerRequest.getCategory()));
			if(versionCreateOrWriteCustomerRequest.getvValidFrom()!=null){
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("validFrom", DateUtil.convertISOToSimpleDateFormat(versionCreateOrWriteCustomerRequest.getvValidFrom())));
			}
			if(versionCreateOrWriteCustomerRequest.getvInvalidFrom()!=null){
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("vInvalidFrom", DateUtil.convertISOToSimpleDateFormat(versionCreateOrWriteCustomerRequest.getvInvalidFrom())));
		    }
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("messageId", String.valueOf(versionCreateOrWriteCustomerRequest.getMessageId())));
			String requestId = RequestContextLocalStore.get().getRequestId();

			// functional service logic to be ported here [Porting work]
			String customerId = versionCreateOrWriteCustomerRequest.getCustomerId();

			SubscriberInfo subscriberinfo = readSubscriber(requestId,customerId, metas);
			if (subscriberinfo == null) {
				logger.error("Subscriber Not Found. msisdn: "
						+ versionCreateOrWriteCustomerRequest.getCustomerId());
				throw new SmException(new ResponseCode(504,"Subscriber Not Found"));
			} else if (ContractState.ACTIVE.name().equals(subscriberinfo.getLocalState())) {
				logger.error("Number is in ACTIVE state in IL DB");
				throw new SmException(new ResponseCode(4020,"Invalid Operation State"));
			} else if(!ContractState.RECYCLED.name().equals(subscriberinfo.getLocalState())){
				logger.error("Number is in Deactive state in IL DB");
				throw new SmException(new ResponseCode(4020,"Invalid Operation State"));
			}else if(!ContractState.GRACE.name().equals(subscriberinfo.getLocalState())){
				logger.error("Number is in GRACE state in IL DB");
				throw new SmException(new ResponseCode(4020,"Invalid Operation State"));
			}
			
			updateSubscriber(requestId, customerId, metas);
			// send response back to web service client [Porting work]
			logger.debug("Response purchase received.. now creating front end response");
			CommandResponseData responseData = createResponse(versionCreateOrWriteCustomerRequest.getUsecase()
					.getOperation(), versionCreateOrWriteCustomerRequest.getUsecase().getModifier(),
					versionCreateOrWriteCustomerRequest.isTransactional());
			exchange.getOut().setBody(responseData);
		} catch (Exception e) {
			logger.error("Error in the processor",e.getClass().getName());
		}
		
	}

	private SubscriberInfo readSubscriber(String requestId,String subscriberId, List<com.ericsson.sef.bes.api.entities.Meta> metas) {
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
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
		logger.info("Check if response received for Read subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;

	}
	private CommandResponseData createResponse(String operationName,
			String modifier, boolean isTransactional) {
		logger.info("Invoking create Response");
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);

		Operation operation = new Operation();
		operation.setName(operationName);
		operation.setModifier(modifier);

		OperationResult operationResult = new OperationResult();

		if (isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}

		return responseData;
	}
	
	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<com.ericsson.sef.bes.api.entities.Meta> metas) throws SmException {
		logger.info("Invoking create subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);

		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			logger.error("Error while acquire()", this.getClass().getName(), e);
		}
		logger.info("Check if response received for create subscriber");

		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		if (subscriberInfo != null) {
			try {
				if (subscriberInfo.getStatus().getCode() > 0) {
					ResponseCode resonseCode = new ResponseCode(subscriberInfo
							.getStatus().getCode(), subscriberInfo.getStatus()
							.getDescription());
					throw new SmException(resonseCode);
				}
			} catch (Exception e) {
				logger.error("subscriberInfo fields are null");
				throw null;
			}
		}
		return subscriberInfo;
	}
}

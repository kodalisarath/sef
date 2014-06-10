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
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.BucketCreateOrWriteRopRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;


public class BucketCreateOrWriteRop implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(CreateOrWriteCustomerProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		try{
			BucketCreateOrWriteRopRequest request = (BucketCreateOrWriteRopRequest) exchange.getIn().getBody();
			
			List<Meta> metas = new ArrayList<Meta>();
			metas.add(new Meta("category", request.getCategory()));
			metas.add(new Meta("key", String.valueOf(request.getKey())));
			metas.add(new Meta("bValidFrom", request.getbValidFrom()));
			metas.add(new Meta("bInvalidFrom", request.getbInvalidFrom()));
			metas.add(new Meta("OnPeakAccountID_FU", request.getOnPeakAccountID_FU()));
			metas.add(new Meta("messageId", String.valueOf(request.getMessageId())));
			String requestId = RequestContextLocalStore.get().getRequestId();
			updateSubscriber(requestId, request.getCustomerId(), metas);
			
			CommandResponseData responseData = createResponse(request.getUsecase().getOperation(), request.getUsecase().getModifier(),request.isTransactional());
			exchange.getOut().setBody(responseData);
		}catch(Exception e){
			logger.error("Error in processor class:",this.getClass().getName(),e);
		}
	
		
	}
	
	
	private CommandResponseData createResponse(String operationName, String modifier, boolean isTransactional) {
		logger.info("Invoking create Response");
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);

		Operation operation = new Operation();
		operation.setName(operationName);
		operation.setModifier(modifier);

		OperationResult operationResult = new OperationResult();

		if(isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}
			
		return responseData;
	}
	private SubscriberInfo updateSubscriber(String requestId, String customer_id,List<Meta> metas) throws SmException {
		logger.info("Invoking create subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.updateSubscriber(requestId, customer_id,metas);
		
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		
		try {
		semaphore.init(0);
		semaphore.acquire();
		} catch(InterruptedException e) {
			logger.error("Error while acquire()",this.getClass().getName(),e);
		}
		
		logger.info("Check if response received for create subscriber");
		
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		if(subscriberInfo != null){
			
			try{
				if(subscriberInfo.getStatus().getCode() > 0){
					ResponseCode resonseCode = new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription());
					throw new SmException(resonseCode);
					}
			}catch(Exception e){
				logger.error("subscriberInfo fields are null");
				throw null;
			}
			
		}
		
		return subscriberInfo;
	}
	
}

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
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.EntireDeleteRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;
public class EntireDeleteSubscriber implements Processor{

	private static final Logger logger = LoggerFactory.getLogger(EntireDeleteSubscriber.class);
	
	@Override
	public void process(Exchange exchange) throws SmException {
		try {
			EntireDeleteRequest request = (EntireDeleteRequest) exchange.getIn().getBody();
			//ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
			String requestId = RequestContextLocalStore.get().getRequestId();
			
			List<Meta> metas = new ArrayList<Meta>();
		//	metas.add(new Meta("federation-profile", "deleteSubscriber"));
			SubscriberInfo subscriberinfo = deleteSubscriber(requestId, request.getCustomerId());
		
			if(subscriberinfo.getStatus() != null && subscriberinfo.getStatus().getCode() >0) {
				throw new SmException(new ResponseCode(13423, "13423#EntireDelete Entity - Customer with primary key Keyname:PK,CustomerId: " + request.getCustomerId()
								+ " does not exist"));
			}
			exchange.getOut().setBody(createResponse(request.getUsecase().getOperation(), request.getUsecase().getModifier(),request.isTransactional()));
		} catch (Exception e) {
			logger.error("Error in the processor class:",e.getClass().getName(),e);
		}
	}

	private CommandResponseData createResponse(String operationName, String modifier,boolean isTransactional) {

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

	private SubscriberInfo deleteSubscriber(String requestId, String customerId) throws Exception {
		logger.info("Invoking delete subscriber on tx-engine subscriber interface");
		
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();

		SubscriberResponseStore.put(requestId, subInfo);
		
		String resultId=iSubscriberRequest.deleteSubscriber(requestId, customerId);	
		
		SubscriberInfo response = new SubscriberInfo();
	      logger.debug("Got past event class....SK");
		  SubscriberResponseStore.put(resultId, response);
		  logger.debug("Got past event class....YEAH");
			
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch(InterruptedException e) {
            
		}
		logger.debug("Awake from sleep.. going to check response in store with id: " +  resultId);
		
		SubscriberInfo purchaseResponse = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		logger.debug("PurchaseResponse recieved here is "+purchaseResponse);
		if(purchaseResponse == null) {
			logger.debug("No response arrived???");
			throw new SmException(ErrorCode.internalServerError);
		}
		else{
			logger.info("Check if response received for update subscriber");
			return purchaseResponse;
		}	
		
		
	}

	
}


package com.ericsson.raso.sef.smart.processor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.ModifyCustomerPreActiveRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class ModifyCustomerPreActive implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(ModifyCustomerPreActive.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		ModifyCustomerPreActiveRequest request = (ModifyCustomerPreActiveRequest) exchange
				.getIn().getBody();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metas = new ArrayList<Meta>();
		String newExpiry = new String();
		
		metas.add(new Meta("EventInfo",String.valueOf(request.getEventInfo())));
		metas.add(new Meta("MessageId",String.valueOf(request.getMessageId())));
		metas.add(new Meta("AccessKey",String.valueOf(request.getAccessKey())));
		SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metas);
		SubscriberInfo subscriberInfo=null;
		
		if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0){
			logger.debug("Inside the if condition for status check");
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		}
		logger.info("Recieved a SubscriberInfo Object and it is not null");
		logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());
		
		if( ContractState.PREACTIVE.getName().equals(subscriberObj.getSubscriber().getContractState())){
			logger.info("In pre-active method");
			String date=subscriberObj.getSubscriber().getMetas().get("PreActiveEndDate");
			if(date != null){
				logger.debug("There is a preActive end date entered and adding days to it now"+date);
				//String newDate=DateUtil.addDaysToDate(date,request.getDaysOfExtension());
				
				// handle the date extension
				SimpleDateFormat metaStoreFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				SimpleDateFormat smartDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				Date currentExpiryDate = metaStoreFormat.parse(date);
				Date newExpiryDate = new Date( currentExpiryDate.getTime() + (request.getDaysOfExtension() * 86400000L));
				newExpiry = smartDateFormat.format(newExpiryDate);
				
				
				
				metas.add(new Meta("PreActiveEndDate", metaStoreFormat.format(newExpiryDate)));
				logger.debug("There is a new preActive end date entered and adding days to it now"+ newExpiry);
			}
			else{
				logger.debug("date is not found");
			}
			subscriberInfo= updateSubscriber(requestId, request.getCustomerId(), metas, Constants.ModifyCustomerPreActive);
			logger.info("Before read subscriber call");
			if(subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() >0){
				throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(), subscriberInfo.getStatus().getDescription()));
			}
		}
		else{
			throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
		}
		CommandResponseData responseData = createResponse(true, newExpiry);
		
		
		String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 
		exchange.getOut().setBody(responseData);
		exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
		//DummyProcessor.response(exchange);
		//exchange.getOut().setBody(subscriberInfo);
		
	}
	
	private CommandResponseData createResponse(boolean isTransactional, String newDate ) {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);

		OperationResult operationResult = new OperationResult();

		if (isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}

		Operation operation = new Operation();
		operation.setModifier("CustomerPreActive");
		operation.setName("Modify");
		operationResult.getOperation().add(operation);

		ParameterList parameterList = new ParameterList();
		List<Object> dataSetList = parameterList.getParameterOrBooleanParameterOrByteParameter();

		DateTimeParameter dParameter = new DateTimeParameter();
		dParameter.setName("PreActiveEndDate");
		dParameter.setModifier(newDate);
		dataSetList.add(dParameter);

		operation.setParameterList(parameterList);

		return responseData;
	}	
	
	
	private SubscriberInfo readSubscriber(String requestId,String customer_id, List<Meta> metas) {
		//logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, customer_id, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
	}
	private SubscriberInfo updateSubscriber(String requestId,String customer_id, List<Meta> metas,String useCase) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas,useCase);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;
	}

	

}

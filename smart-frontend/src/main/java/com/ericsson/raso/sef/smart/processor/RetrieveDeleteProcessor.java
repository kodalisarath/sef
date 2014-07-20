package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.usecase.RetrieveDeleteRequest;
import com.ericsson.raso.sef.watergate.FloodGate;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;



public class RetrieveDeleteProcessor implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(RetrieveDeleteProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
			RetrieveDeleteRequest request = (RetrieveDeleteRequest) exchange.getIn().getBody();
			String requestId = RequestContextLocalStore.get().getRequestId();
			SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), new ArrayList<Meta>());
			if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0){
				logger.debug("Inside the if condition for status check");
				throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
			}
			if(subscriberObj.getSubscriber() != null){
				SubscriberInfo subscriberInfo= updateSubscriber(requestId, request.getCustomerId(), new ArrayList<Meta>(), Constants.RetrieveDelete);
				  exchange.getOut().setBody(subscriberInfo);
					if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() >0) {
						logger.debug("Problem in persisting");
						throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription()));
					}
			}
			//DummyProcessor.response(exchange);
			CommandResponseData cr = this.createResponse(true);

			String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 
			
			logger.error("FloodGate acknowledging exgress...");
			FloodGate.getInstance().exgress();
			
			exchange.getOut().setBody(cr);
			
			exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
			}
	
	private CommandResponseData createResponse(boolean isTransactional) throws SmException {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);
		OperationResult operationResult = new OperationResult();
		Operation operation = new Operation();
		operation.setName("RetrieveDelete");
		operation.setModifier("ServiceAccessKey");
		operationResult.getOperation().add(operation);
		ParameterList parameterList = new ParameterList();
		EnumerationValueParameter evp = new EnumerationValueParameter();
		evp.setName("Category");
		evp.setValue("ONLINE");
		ListParameter listParameter = new ListParameter();
		listParameter.getElementOrBooleanElementOrByteElement().add(evp);
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(listParameter);
		operation.setParameterList(parameterList);
		
		if(isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}
		
		return responseData;
	}
	
	
	
	
	private SubscriberInfo deleteSubscriber(String requestId,String subscriberId) {
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.deleteSubscriber(requestId, subscriberId);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
		semaphore.init(0);
		semaphore.acquire();
		} catch(InterruptedException e) {
		}
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
		
	}
	/*public CommandResponseData response(String requestId,String subscriberId, 
			String operationName, 
			String modifier,
			boolean transactionalOperation)throws ParseException {
		subscriberId
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);

		Operation operation = new Operation();
		operation.setName(operationName);
		operation.setModifier(modifier);

		OperationResult operationResult = new OperationResult();
		operationResult.getOperation().add(operation);
		
		
		if(transactionalOperation) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}
		
		//	SubscriberManagement subscriberManagement = SmartContext.getSubscriberManagement();
		
			List<Meta> metaList = new ArrayList<Meta>();
			metaList.add(new Meta("Key",null));
			metaList.add(new Meta("KeyType",null));
			metaList.add(new Meta("vValidFrom",null));
			metaList.add(new Meta("Category",null));
			metaList.add(new Meta("vInvalidFrom",null));
			metaList.add(new Meta("OwningCustomerId",null));
		
			
			//Subscriber subscriber = subscriberManagement.getSubscriberProfile(customerId, keys);
			
			SubscriberInfo subscriberInfo = readSubscriber(requestId, subscriberId, metaList);
			Map<String,String> metaMap = subscriberInfo.getMetas();
		
			ParameterList paramList = new ParameterList();
			
			operation.setParameterList(paramList);
	
			if(metaMap.containsKey("Key"))
			{
				StringParameter key = new StringParameter();
				key.setName("Key");
				key.setValue(metaMap.get("Key"));
				paramList.getParameterOrBooleanParameterOrByteParameter().add(key);
			}
			
			if(metaMap.containsKey("KeyType"))
			{
				StringParameter key = new StringParameter();
				key.setName("KeyType");
				key.setValue(metaMap.get("KeyType"));
				paramList.getParameterOrBooleanParameterOrByteParameter().add(key);
			}
			
			if(metaMap.containsKey("vValidFrom"))
			{
				DateTimeParameter vValidFrom = new DateTimeParameter();
				vValidFrom.setName("vValidFrom");
				XMLGregorianCalendar value = null;
				if(metaMap.get("vValidFrom") != null && metaMap.get("vValidFrom") !="") {
					
					IConfig config = SefCoreServiceResolver.getConfigService();
				    Date vValidFromDate =DateUtil.convertStringToDate(metaMap.get("vValidFrom"), config.getValue("GLOBAL", "dateFormat"));
				    value = DateUtil.convertDateToUTCtime(vValidFromDate);
				}
				vValidFrom.setValue(value);
				paramList.getParameterOrBooleanParameterOrByteParameter().add(vValidFrom);
				
			}
			
			
			if(metaMap.containsKey("Category"))
			{
				StringParameter key = new StringParameter();
				key.setName("Category");
				key.setValue(metaMap.get("Category"));
				paramList.getParameterOrBooleanParameterOrByteParameter().add(key);
			}
			
			
			if(metaMap.containsKey("vInvalidFrom"))
			{
				StringParameter key = new StringParameter();
				key.setName("vInvalidFrom");
				key.setValue(metaMap.get("vInvalidFrom"));
				paramList.getParameterOrBooleanParameterOrByteParameter().add(key);
			}
			
			
			
			if(metaMap.containsKey("KeyType"))
			{
				StringParameter key = new StringParameter();
				key.setName("KeyType");
				key.setValue(metaMap.get("KeyType"));
				paramList.getParameterOrBooleanParameterOrByteParameter().add(key);
			}
			
			
			
			if(metaMap.containsKey("OwningCustomerId"))
			{
				StringParameter key = new StringParameter();
				key.setName("OwningCustomerId");
				key.setValue(metaMap.get("OwningCustomerId"));
				paramList.getParameterOrBooleanParameterOrByteParameter().add(key);
			}
			
			
			
			
			
			
			for(Meta meta : metas) {
				if(meta.getKey().equals("Key")) {
					StringParameter key = new StringParameter();
					key.setName("Key");
					key.setValue(meta.getValue());
					paramList.getParameterOrBooleanParameterOrByteParameter().add(key);
				}
				if(meta.getKey().equals("KeyType")) {
					IntParameter keyType = new IntParameter();
					keyType.setName("KeyType");
					keyType.setValue(Integer.parseInt(meta.getValue()));
					paramList.getParameterOrBooleanParameterOrByteParameter().add(keyType);
				}
				if(meta.getKey().equals("vValidFrom")) {
					DateTimeParameter vValidFrom = new DateTimeParameter();
					vValidFrom.setName("vValidFrom");
					XMLGregorianCalendar value = null;
					if(meta.getValue() != null && meta.getValue() !="") {
					    Date vValidFromDate = new StringToDateTransformer(SmartContext.getProperty(SmartContext.DATE_FORMAT)).transform(meta.getValue());
					    value = DateUtil.convertDateToUTCtime(vValidFromDate);
					}
					vValidFrom.setValue(value);
					paramList.getParameterOrBooleanParameterOrByteParameter().add(vValidFrom);
					
				}
				if(meta.getKey().equals("Category")) {
					EnumerationValueParameter category = new EnumerationValueParameter();
					category.setName("Category");
					category.setValue(meta.getValue());
					paramList.getParameterOrBooleanParameterOrByteParameter().add(category);
				}
				if(meta.getKey().equals("vInvalidFrom")) {
					SymbolicParameter vInvalidFrom = new SymbolicParameter();
					vInvalidFrom.setName("vInvalidFrom");
					vInvalidFrom.setValue(meta.getValue());
					paramList.getParameterOrBooleanParameterOrByteParameter().add(vInvalidFrom);
				}
				if(meta.getKey().equals("OwningCustomerId")) {
					StringParameter owningCustomerId = new StringParameter();
					owningCustomerId.setName("OwningCustomerId");
					owningCustomerId.setValue(meta.getValue());
					paramList.getParameterOrBooleanParameterOrByteParameter().add(owningCustomerId);
				}
			}
		
		
		return responseData;
	}*/


	private SubscriberInfo readSubscriber(String requestId, String subscriberId, List<Meta> metas){
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
		semaphore.init(0);
		semaphore.acquire();
		} catch(InterruptedException e) {
		}
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
		
	}
	
	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<Meta> metas,String useCase) throws SmException {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		logger.debug("Requesting ");
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas,useCase);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			logger.error("Error while calling acquire()");
		}
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		
		return subscriberInfo;
	}

}

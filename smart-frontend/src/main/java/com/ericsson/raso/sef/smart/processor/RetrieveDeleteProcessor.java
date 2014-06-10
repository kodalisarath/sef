package com.ericsson.raso.sef.smart.processor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.RetrieveDeleteRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;



public class RetrieveDeleteProcessor implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(RetrieveDeleteProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		try {
			RetrieveDeleteRequest request = (RetrieveDeleteRequest) exchange.getIn().getBody();
			String requestId = RequestContextLocalStore.get().getRequestId();
		
			
			SubscriberInfo subscriberinfo = readSubscriber(requestId, request.getCustomerId(),null);
			
			
	if(subscriberinfo ==null)
	{
	logger.error("Subscriber Not Found. msisdn: "
			+ request.getCustomerId());	
	throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
	}
			
			//SmartContext.getSubscriberManagement().changeContractState(request.getCustomerId(), ContractState.READY_TO_DELETE, null);
			ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
			iSubscriberRequest.handleLifeCycle(requestId, request.getCustomerId(), ContractState.READY_TO_DELETE.getName(), null);
			
			
			exchange.getOut().setBody(response(requestId,request.getCustomerId(), request.getUsecase().getOperation(), request.getUsecase().getModifier(), request.isTransactional() ));
		} catch (Exception e) {
			logger.error("Error in processor class:",e.getClass().getName(),e);
		}
		 
	}
	
	
	public CommandResponseData response(String requestId,String subscriberId, 
			String operationName, 
			String modifier,
			boolean transactionalOperation)throws ParseException {

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
			metaList.add(new Meta("category",null));
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
			
			
			if(metaMap.containsKey("category"))
			{
				StringParameter key = new StringParameter();
				key.setName("category");
				key.setValue(metaMap.get("category"));
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
			
			
			
			
			
			
			/*for(Meta meta : metas) {
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
				if(meta.getKey().equals("category")) {
					EnumerationValueParameter category = new EnumerationValueParameter();
					category.setName("category");
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
*/		
		
		return responseData;
	}


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
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
		
	}
	
	

}

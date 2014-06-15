package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.Collection;
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
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.ModifyTaggingRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class ModifyTagging implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(ModifyTagging.class);

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws SmException {
		ModifyTaggingRequest request = (ModifyTaggingRequest) exchange
				.getIn().getBody();
		String requestId = RequestContextLocalStore.get().getRequestId();
		Integer tag = Integer.valueOf(request.getTagging());
		logger.error("Subscriber Tagging Code is:", tag);
//
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("CustomerId", String.valueOf(request.getCustomerId())));
		metas.add(new Meta("AccessKey", String.valueOf(request.getAccessKey())));
		metas.add(new Meta("Tagging", String.valueOf(request.getTagging())));
		metas.add(new Meta("EventInfo", String.valueOf(request.getEventInfo())));
		metas.add(new Meta("MessageId", String.valueOf(request.getMessageId())));
		
//		Object[] objectArray = (Object[]) exchange.getIn().getBody(Object[].class);
//		metas.add(new Meta("customerId", (String) objectArray[0].toString()));
//		metas.add(new Meta("accessKey", (String) objectArray[1].toString()));
//		metas.add(new Meta("tagging", (String)objectArray[2].toString()));
//		metas.add(new Meta("eventInfo", (String)objectArray[3].toString()));
//		metas.add(new Meta("messageId", (String)objectArray[4].toString()));

		String tagging = String.valueOf(request.getTagging());
		//Integer tag = Integer.valueOf(tagging);
		switch (tag) {
			case 0: metas.add(new Meta("HANDLE_LIFE_CYCLE", "resetBit"));
					break;
			case 1: metas.add(new Meta("HANDLE_LIFE_CYCLE", "forcedDeleteBit"));
					break;
			case 2: metas.add(new Meta("HANDLE_LIFE_CYCLE", "barGeneralBit"));
					break;
			case 3: metas.add(new Meta("HANDLE_LIFE_CYCLE", "barIrmBit"));
					break;
			case 4:	metas.add(new Meta("HANDLE_LIFE_CYCLE", "barOtherBit"));
					break;
			case 5:	metas.add(new Meta("HANDLE_LIFE_CYCLE", "specialFraudBit"));
					break;
			case 6:	metas.add(new Meta("HANDLE_LIFE_CYCLE", "accountBlockingBit"));
					break;
			case 7:	metas.add(new Meta("HANDLE_LIFE_CYCLE", "recycleBit"));
					break;
			default:
					metas.add(new Meta("HANDLE_LIFE_CYCLE", "invalidBit"));
					break;
		}
		logger.debug("Usecase Metas: " + metas);

		SubscriberInfo updateSubscriberInfo;

		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subscriberObj = readSubscriber(requestId, request.getCustomerId(), metas);

		if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0)
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		
		updateSubscriberInfo = updateSubscriber(requestId, request.getCustomerId(), metas, Constants.ModifyTagging);

		if (updateSubscriberInfo.getStatus().getCode() == 0) {
			Collection<Meta> updateInfoMetasMap = (Collection<Meta>) updateSubscriberInfo.getMetas();
			List<Meta> updateInfoMetaList = convertToMetaList(updateInfoMetasMap);
			updateSubscriberInfo = updateSubscriberHandleLifeCycle(updateSubscriberInfo.getRequestId(),updateSubscriberInfo.getMsisdn(), updateInfoMetaList);
			if (updateSubscriberInfo.getStatus() != null) {
				throw ExceptionUtil.toSmException(new ResponseCode(updateSubscriberInfo.getStatus().getCode(),updateSubscriberInfo.getStatus().getDescription()));
			}else{
				DummyProcessor.response(exchange);
			} 
		} else if(updateSubscriberInfo.getStatus() != null){
			throw ExceptionUtil.toSmException(new ResponseCode(updateSubscriberInfo.getStatus().getCode(),updateSubscriberInfo.getStatus().getDescription()));
		}
		exchange.getOut().setBody(createResponse(tag, request.isTransactional()));
	}

	private CommandResponseData createResponse(int tag, boolean isTransactional) {
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
		operation.setModifier("Tagging");
		operation.setName("Modify");
		operationResult.getOperation().add(operation);

		ParameterList parameterList = new ParameterList();
		List<Object> dataSetList = parameterList.getParameterOrBooleanParameterOrByteParameter();

		IntParameter intParameter = new IntParameter();
		intParameter.setName("ResultTagging");
		intParameter.setValue(tag);
		dataSetList.add(intParameter);

		operation.setParameterList(parameterList);

		return responseData;
	}


	private SubscriberInfo updateSubscriber(String requestId, String customer_id, List<Meta> metas,String processRequestKey) throws SmException {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		logger.debug("Requesting ");
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas, processRequestKey);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			logger.error("Error while calling acquire()");
		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
	}

	private SubscriberInfo updateSubscriberHandleLifeCycle(String requestId, String msisdn, List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface for hande modifyhandle life cycle");

		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();

		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.handleLifeCycle(requestId, msisdn, null, metas);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;

	}
	
	private SubscriberInfo readSubscriber(String requestId,String customer_id, List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
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
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		//SubscriberResponseStore.get(requestId);
		return subscriberInfo;
	}
	
	private List<Meta> convertToMetaList(Collection<Meta> subscriberMetas) {
		List<Meta> metaList=new ArrayList<Meta>();
		if(subscriberMetas != null){
			List<Meta> subscribermetaList=new ArrayList<Meta>(subscriberMetas);
			for(Meta metaSubscriber:subscribermetaList){
				Meta meta=new Meta();
				meta.setKey(metaSubscriber.getKey());
				meta.setValue(metaSubscriber.getValue());
				metaList.add(meta);
			}	
		}

		return metaList;
	}
}

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
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
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

	@Override
	public void process(Exchange exchange) throws SmException {

		ModifyTaggingRequest request = (ModifyTaggingRequest) exchange.getIn().getBody();
		String requestId = RequestContextLocalStore.get().getRequestId();
		validateSubscriber(requestId, request.getCustomerId());

		Integer tag = Integer.valueOf(request.getTagging());

		// SubscriberManagement subscriberManagement = SmartContext.getSubscriberManagement();
		List<String> keys = new ArrayList<String>();
		keys.add(SmartConstants.GRACE_ENDDATE);

		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("tagging", String.valueOf(request.getTagging())));
		metas.add(new Meta("eventInfo", String.valueOf(request.getEventInfo())));
		metas.add(new Meta("messageId", String.valueOf(request.getMessageId())));

		String tagging = String.valueOf(request.getTagging());
		switch(tagging) {
			case "0":
				metas.add(new Meta("HANDLE_LIFE_CYCLE", "resetBit"));
				break;
			case "1":
				metas.add(new Meta("HANDLE_LIFE_CYCLE", "forcedDeleteBit"));
				break;
			case "2":
				metas.add(new Meta("HANDLE_LIFE_CYCLE", "barGeneralBit"));
				break;
			case "3":
				metas.add(new Meta("HANDLE_LIFE_CYCLE", "barIrmBit"));
				break;
			case "4":
				metas.add(new Meta("HANDLE_LIFE_CYCLE", "barOtherBit"));
				break;
			case "5":
				metas.add(new Meta("HANDLE_LIFE_CYCLE", "specialFraudBit"));
				break;
			case "6":
				metas.add(new Meta("HANDLE_LIFE_CYCLE", "accountBlockingBit"));
				break;
			case "7":
				metas.add(new Meta("HANDLE_LIFE_CYCLE", "recycleBit"));
				break;
			default:
				throw ExceptionUtil.toSmException(new ResponseCode(4020, "Subscriber Not Found"));
		}
		logger.debug("Usecase Metas: " + metas);

		// subscriberManagement.updateSubscriber(request.getCustomerId(), metas);

		updateSubscriber(requestId, request.getCustomerId(), metas);

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

	private void validateSubscriber(String requestId, String customerId) throws SmException {

		SubscriberInfo subscriberInfo = readSubscriber(requestId, customerId, null);

		// SubscriberManagement subscriberManagement = SmartContext.getSubscriberManagement();
		// Subscriber subscriber = subscriberManagement.getSubscriberProfile(customerId, null);
		if (subscriberInfo != null && subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() == 504) {
			throw new SmException(new ResponseCode(504, "Invalid Account"));
		
		}
		logger.debug("If I am here, then it means the subscriber exists or may be other problem......" );
	}

	private SubscriberInfo updateSubscriber(String requestId, String customer_id, List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.handleLifeCycle(requestId, customer_id, null, metas);
		
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

	private SubscriberInfo readSubscriber(String requestId, String subscriberId, List<Meta> metas) {
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
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

}

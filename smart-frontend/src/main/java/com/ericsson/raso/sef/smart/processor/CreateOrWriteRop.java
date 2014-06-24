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
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.CreateOrWriteRopRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class CreateOrWriteRop implements Processor {
	private static final Logger logger = LoggerFactory
			.getLogger(CreateOrWriteRop.class);

	@Override
	public void process(Exchange exchange) throws Exception {
	

			logger.info("CreateOrWriteRop: process()");
			CreateOrWriteRopRequest request = (CreateOrWriteRopRequest) exchange
					.getIn().getBody();
			//logger.debug("");

			// SubscriberManagement subscriberManagement =
			// SmartContext.getSubscriberManagement();
			List<Meta> metas = new ArrayList<Meta>();
			metas.add(new Meta("Key", String.valueOf(request.getKey())));
			metas.add(new Meta("IsSmsAllowed", String.valueOf(request.getIsSmsAllowed())));
			metas.add(new Meta("IsUSCAllowed", String.valueOf(request.getIsUSCAllowed())));
			metas.add(new Meta("IsGPRSUsed", String.valueOf(request.getIsGPRSUsed())));
			metas.add(new Meta("IsLastTransactionEnqUsed", String.valueOf(request.getIsLastTransactionEnqUsed())));
			metas.add(new Meta("LanguageID", String.valueOf(request.getLanguageId())));
			/*if (request.getActiveEndDate() != null)
				metas.add(new Meta("activeEndDate", DateUtil
						.convertISOToSimpleDateFormat(request
								.getActiveEndDate())));
			//These are not getting from the DB,
OwningCustomerId
			if (request.getGraceEndDate() != null)
				metas.add(new Meta("graceEndDate",
						DateUtil.convertISOToSimpleDateFormat(request
								.getGraceEndDate())));*/

			String preActiveEndDate = DateUtil
					.convertISOToSimpleDateFormat(request.getPreActiveEndDate());

			
			metas.add(new Meta("PreActiveEndDate", String.valueOf(preActiveEndDate)));
			metas.add(new Meta("FirstCallDate", request.getFirstCallDate()));
			metas.add(new Meta("IsFirstCallPassed", String.valueOf(request.getIsFirstCallPassed())));
			metas.add(new Meta("LastKnownPeriod", String.valueOf(request.getLastKnownPeriod())));
			metas.add(new Meta("category", String.valueOf(request.getCategory())));
			metas.add(new Meta("MessageId", String.valueOf(request.getMessageId())));
			metas.add(new Meta("s_CRMTitle", String.valueOf(request.getS_CRMTitle())));
			metas.add(new Meta("c_TaggingStatus", String.valueOf(request.getC_TaggingStatus())));
			metas.add(new Meta("MessageId", String.valueOf(request.getMessageId())));
			metas.add(new Meta("IsBalanceClearanceOnOutpayment ", String.valueOf(request.isIsBalanceClearanceOnOutpayment())));
			metas.add(new Meta("IsCFMOC",String.valueOf(request.isIsCFMOC())));
			metas.add(new Meta("IsCollectCallAllowed",String.valueOf(request.isIsCollectCallAllowed())));
			metas.add(new Meta("IsOperatorCollectCallAllowed",String.valueOf(request.isIsOperatorCollectCallAllowed())));
			metas.add(new Meta("IsLocked",String.valueOf(request.isIsLocked())));
			// subscriberManagement.updateSubscriber(request.getCustomerId(),
			// metas);
			String requestId = RequestContextLocalStore.get().getRequestId();

			SubscriberInfo subscriberInfo = updateSubscriber(requestId,request.getCustomerId(), metas,Constants.CreateOrWriteROP);
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() >0) {
			logger.error("Response about to send with SOAP ",subscriberInfo.getStatus().getDescription());
			throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(), subscriberInfo.getStatus().getDescription()));
			
		}
		DummyProcessor.response(exchange);
		//exchange.getOut().setBody(subscriberInfo);
	}

//	private CommandResponseData createResponse(String operationName,
//			String modifier, boolean isTransactional) {
//		logger.info("Invoking create Response");
//		CommandResponseData responseData = new CommandResponseData();
//		CommandResult result = new CommandResult();
//		responseData.setCommandResult(result);
//
//		Operation operation = new Operation();
//		operation.setName(operationName);
//		operation.setModifier(modifier);
//
//		OperationResult operationResult = new OperationResult();
//
//		if (isTransactional) {
//			TransactionResult transactionResult = new TransactionResult();
//			result.setTransactionResult(transactionResult);
//			transactionResult.getOperationResult().add(operationResult);
//		} else {
//			result.setOperationResult(operationResult);
//		}
//
//		return responseData;
//	} 

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

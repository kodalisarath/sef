package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.VersionCreateOrWriteCustomerDummyRequest;
import com.ericsson.raso.sef.smart.usecase.VersionCreateOrWriteCustomerRequest;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
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

			// functional service logic to be ported here [Porting work]
			String customerId = versionCreateOrWriteCustomerRequest.getCustomerId();
			VersionCreateOrWriteCustomerDummyRequest dummyRequest = new VersionCreateOrWriteCustomerDummyRequest();
			SubscriberService subscriberService = SefCoreServiceResolver
					.getSusbcriberStore();
			// get subscriber based on msisdn
			// Subscriber subscriber = subscriberService.getSubscriber(customerId);
			Subscriber subscriber = subscriberService.getSubscriberByUserId(UniqueIdGenerator.generateId(), customerId);
			// if subscriber is unknown
			if (subscriber == null) {
				logger.error("Subscriber is UNKNOWN");
				throw new SmException(new ResponseCode(504,
						"Invalid Operation State"));
			}else{
			// if subscriber is valid
			Collection<Meta> metas = subscriber.getMetas();
			dummyRequest.setSubscriberMeta(metas);
			String subscriberStatus = subscriber.getContractState().getName();
			if (subscriberStatus.equalsIgnoreCase("PRE")) {
				// update subscriber and subscriber_meta table with version
				for (Meta meta : dummyRequest.getSubscriberMeta()) {
					String key = meta.getKey();
					String value = meta.getValue();
					metas.add(new Meta("category",
							versionCreateOrWriteCustomerRequest.getCategory()));
					if (key.equalsIgnoreCase("vValidFrom")) {
						meta.setValue(DateUtil
								.convertISOToSimpleDateFormat(versionCreateOrWriteCustomerRequest
										.getvValidFrom()));
					}
					if (key.equalsIgnoreCase("vInvalidFrom")) {
						meta.setValue(DateUtil
								.convertISOToSimpleDateFormat(versionCreateOrWriteCustomerRequest
										.getvInvalidFrom()));
					}
					metas.add(new Meta("messageId", String
							.valueOf(versionCreateOrWriteCustomerRequest
									.getMessageId())));
				}
				DummyProcessor.response(exchange);
			} else {
				if (subscriberStatus.equalsIgnoreCase("ACT")) {
					logger.error("Number is in Active State");
					throw new SmException(new ResponseCode(4020,
							"Invalid Operation"));
				}
				if (subscriberStatus.equalsIgnoreCase("RECYCLE")) {
					logger.error("Number is in Deactive state in IL DB");
					throw new SmException(new ResponseCode(4020,
							"Invalid Operation"));
				}
				// return response code for Subscriber State GRACE with Zero Balance
				// or Expired ActiveEndDate
				if (subscriberStatus.equalsIgnoreCase("GRACE")) {
					// check with zero balance and activeEndDate expiry date
					// subscriberRequestId =
					// subscriberRequest.readSubscriber(requestId, subscriberId,
					// metas);
					logger.error("Number is in Grace state in IL DB");
					throw new SmException(new ResponseCode(4020,
							"Invalid Operation"));
				}
			  }
			}

			// delegate to backend for processing [Standard]
			logger.debug("Got event class....");
			String requestId = RequestContextLocalStore.get().getRequestId();
			ISubscriberRequest subscriberRequest = SmartServiceResolver.getSubscriberRequest();
			List<com.ericsson.sef.bes.api.entities.Meta> list = null;
			Collection<Meta> convertMetas = dummyRequest.getSubscriberMeta();
			for (Meta meta : convertMetas) {
				list = new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
				list.addAll((Collection<? extends com.ericsson.sef.bes.api.entities.Meta>) meta);
			}
			String correlationId = subscriberRequest.updateSubscriber(requestId,
					customerId, list);
			logger.debug("Got past event class....");
			SubscriberInfo response = new SubscriberInfo();
			SubscriberResponseStore.put(correlationId, response);
			ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
					.getSemaphore(requestId);

			try {
				semaphore.init(0);
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.debug("Exception while sleep     :" + e.getMessage());
			}

			// Response is received here [Standard]
			logger.debug("Awake from sleep.. going to check response in store with id: "
					+ correlationId);

			// functional logic of response handling here.... [Porting work]
			SubscriberInfo subInfo = (SubscriberInfo) SubscriberResponseStore
					.get(correlationId);
			// send response back to web service client [Porting work]
			logger.debug("Response purchase received.. now creating front end response");
			CommandResponseData responseData = createResponse(subInfo, response);
			exchange.getOut().setBody(responseData);
		} catch (Exception e) {
			logger.error("Error in the processor",e.getClass().getName());
		}
		
	}

	private CommandResponseData createResponse(SubscriberInfo subInfo,
			SubscriberInfo response) {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);
		OperationResult operationResult = new OperationResult();
		// variable not with subscriberRequest
		/*
		 * boolean isTransactional = false; if(isTransactional) {
		 */
		TransactionResult transactionResult = new TransactionResult();
		result.setTransactionResult(transactionResult);
		transactionResult.getOperationResult().add(operationResult);
		/*
		 * } else { result.setOperationResult(operationResult); }
		 */

		Operation operation = new Operation();
		operation.setModifier("CustomerPreActive");
		operation.setName("Modify");
		operationResult.getOperation().add(operation);

		ParameterList parameterList = new ParameterList();
		operation.setParameterList(parameterList);

		DateTimeParameter dateTimeParameter = new DateTimeParameter();
		dateTimeParameter.setName(SmartConstants.RESPONSE_PREACTIVE_ENDDATE);

		/* java.util.Date preActiveEndDate = null; */
		dateTimeParameter.setValue(DateUtil.convertDateToUTCtime(new Date()));
		operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter()
				.add(dateTimeParameter);
		return responseData;
	}
}

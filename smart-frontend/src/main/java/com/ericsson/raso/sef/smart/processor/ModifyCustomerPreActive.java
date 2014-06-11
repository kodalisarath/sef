package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.ModifyCustomerPreActiveRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
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

	private static final Logger logger = LoggerFactory
			.getLogger(ModifyCustomerPreActive.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		ModifyCustomerPreActiveRequest request = (ModifyCustomerPreActiveRequest) exchange
				.getIn().getBody();

		IConfig config = SefCoreServiceResolver.getConfigService();

		// SubscriberManagement subscriberManagement =
		// SmartContext.getSubscriberManagement();
		// List<String> keys = new ArrayList<String>();
		// keys.add("PreActiveEndDate");

		// Subscriber subscriber =
		// subscriberManagement.getSubscriberProfile(request.getCustomerId(),
		// keys);
		String requestId = RequestContextLocalStore.get().getRequestId();
		SubscriberInfo subscriberinfo = readSubscriber(requestId,
				request.getCustomerId(), null);

		logger.info("Manila subscriberinfo for msisdn:" + request.getCustomerId()
				+ " is " + subscriberinfo);

		
		if (subscriberinfo == null  ) {
			logger.error("Manila Log: Subscriber Not Found. msisdn: "
					+ request.getCustomerId());

			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);

			// throw new SmException(new
			// ResponseCode(ErrorCode.invalidAccount.getCode(),
			// ErrorCode.invalidAccount.getMessage()
			// +" :msisdn "+request.getCustomerId()));

		}
		else if (ContractState.apiValue(ContractState.PREACTIVE.name()).equals(subscriberinfo.getLocalState())) {

			logger.error("Manila Log: Subscriber should be in preactive state to extend the preActiveEndDate. msisdn: "
					+ request.getCustomerId() +", but Current Status is "+subscriberinfo.getLocalState());
			// throw new SmException(new
			// ResponseCode(ErrorCode.notPreActive.getCode(),
			// ErrorCode.notPreActive.getMessage()
			// +" :msisdn "+request.getCustomerId()));

			throw ExceptionUtil.toSmException(ErrorCode.notPreActive);
		}
		else if(subscriberinfo !=null )
		{
			TransactionStatus status = subscriberinfo.getStatus();
			if(status !=null){
				int statusCode = status.getCode();
				logger.debug("Manila error Status Code is "+statusCode+" :msisdn "+request.getCustomerId() +" Read Subscriber Failed");
				if(statusCode != 0)
					throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
					//throw new SmException(new ResponseCode(ErrorCode.invalidAccount.getCode(), ErrorCode.invalidAccount.getMessage() ));
					
			}
			
			
		}
		Date preActiveEndDate = null;
		String preActiveEndDateStr = null;

		// preActiveEndDateStr = getMetaValue(subscriber.getMetas(),
		// SmartConstants.PREACTIVE_ENDDATE);
		preActiveEndDateStr = subscriberinfo.getMetas().get(
				SmartConstants.PREACTIVE_ENDDATE);
		String milliSecMultiplier = config.getValue("GLOBAL",
				SmartConstants.MILLISEC_MULTIPLIER);

		preActiveEndDate = DateUtil.convertStringToDate(preActiveEndDateStr,
				config.getValue("GLOBAL", "dateFormat"));

		preActiveEndDate = new Date(preActiveEndDate.getTime()
				+ Long.valueOf(request.getDaysOfExtension())
				* Long.valueOf(milliSecMultiplier));

		logger.info("new PreActiveEndDate for msisdn:"
				+ request.getCustomerId() + " is "
				+ preActiveEndDate.toString());
		List<Meta> metas = new ArrayList<Meta>();
		// DateToStringTransformer transformer = new
		// DateToStringTransformer(SmartContext.getProperty(SmartContext.DATE_FORMAT));
		preActiveEndDateStr = DateUtil.convertDateToString(preActiveEndDate,
				config.getValue("GLOBAL", "dateFormat"));
		Meta preActiveEndDateMeta = new Meta(SmartConstants.PREACTIVE_ENDDATE,
				preActiveEndDateStr);
		metas.add(preActiveEndDateMeta);
		// metas.add(new Meta("federation-profile", "shelfLifeExtension"));

		updateSubscriber(requestId, request.getCustomerId(), metas);

		CommandResponseData responseData = createResponse(preActiveEndDate,
				request.isTransactional());
		/* @To Do. To be completed after scheduler is ready. */
		// RescheduleRecycleCommand command = new
		// RescheduleRecycleCommand(subscriber, preActiveEndDate);
		// command.execute();
		exchange.getOut().setBody(responseData);
	}

	private CommandResponseData createResponse(Date preActiveEndDate,
			boolean isTransactional) {
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
		operation.setParameterList(parameterList);

		DateTimeParameter dateTimeParameter = new DateTimeParameter();
		dateTimeParameter.setName(SmartConstants.RESPONSE_PREACTIVE_ENDDATE);

		dateTimeParameter.setValue(DateUtil
				.convertDateToUTCtime(preActiveEndDate));
		operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter()
				.add(dateTimeParameter);
		return responseData;
	}

	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
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

		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;
	}

	private SubscriberInfo readSubscriber(String requestId,
			String subscriberId, List<Meta> metas) {
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
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
		logger.info("Check if response received for read subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;

	}
}

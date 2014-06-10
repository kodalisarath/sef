package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.ModifyCustomerGraceRequest;
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

public class ModifyCustomerGrace implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(ModifyCustomerGrace.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		
			
			ModifyCustomerGraceRequest request = (ModifyCustomerGraceRequest) exchange.getIn().getBody();
			
			String requestId = RequestContextLocalStore.get().getRequestId();
			SubscriberInfo subscriberInfo = readSubscriber(requestId,request.getCustomerId(),null);
			
			if (!ContractState.PREACTIVE.name().equals(subscriberInfo.getLocalState())) {
				
				logger.error("Subscriber should be in GRACE state to extend the graceEndDate. msisdn: "
						+ request.getCustomerId());	
				throw ExceptionUtil.toSmException(ErrorCode.notPreActive);
			}
			List<String> keys = new ArrayList<String>();
			keys.add(SmartConstants.GRACE_ENDDATE);
			
			List<Meta> metas = new ArrayList<Meta>();
			metas.add(new Meta("daysOfExtension" , String.valueOf(request.getDaysOfExtension())));
			metas.add(new Meta("eventInfo" , String.valueOf(request.getEventInfo())));
			metas.add(new Meta("messageId" , String.valueOf(request.getMessageId())));
			
			metas.add(new Meta("federation-profile", "extendGracePeriod"));
			
			//subscriberManagement.updateSubscriber(request.getCustomerId(), metas);
		    updateSubscriber(requestId, request.getCustomerId(), metas);
			
			//Subscriber subscriber = subscriberManagement.getSubscriberProfile(request.getCustomerId(), keys);
			
			String graceEndDateStr = subscriberInfo.getMetas().get(SmartConstants.GRACE_ENDDATE); 
			
			//Date graceEndDate = new StringToDateTransformer(SmartContext.getProperty(SmartContext.DATE_FORMAT)).transform(graceEndDateStr);
			IConfig config = SefCoreServiceResolver.getConfigService();
			Date graceEndDate =DateUtil.convertStringToDate(graceEndDateStr, config.getValue("GLOBAL", "dateFormat"));
			exchange.getOut().setBody(createResponse(graceEndDate,request.isTransactional()));
		
			
			
		
	}
		private CommandResponseData createResponse(Date graceEndDate,boolean isTransactional) {
			CommandResponseData responseData = new CommandResponseData();
			CommandResult result = new CommandResult();
			responseData.setCommandResult(result);
			
			OperationResult operationResult = new OperationResult();

			if(isTransactional) {
				TransactionResult transactionResult = new TransactionResult();
				result.setTransactionResult(transactionResult);
				transactionResult.getOperationResult().add(operationResult);
			} else {
				result.setOperationResult(operationResult);
			}
			
			Operation operation = new Operation();
			operation.setModifier("CustomerGrace");
			operation.setName("Modify");
			operationResult.getOperation().add(operation);
			
			ParameterList parameterList = new ParameterList();
			operation.setParameterList(parameterList);
			
			DateTimeParameter dateTimeParameter = new DateTimeParameter();
			dateTimeParameter.setName(SmartConstants.RESPONSE_GRACE_ENDDATE);	
	        
			dateTimeParameter.setValue(DateUtil.convertDateToUTCtime(graceEndDate));
			operation.getParameterList().getParameterOrBooleanParameterOrByteParameter().add(dateTimeParameter);
			return responseData;
		}
		private SubscriberInfo updateSubscriber(String requestId, String customer_id,List<Meta> metas) {
			logger.info("Invoking update subscriber on tx-engine subscriber interface");
			ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
			SubscriberInfo subInfo = new SubscriberInfo();
			SubscriberResponseStore.put(requestId, subInfo);
			iSubscriberRequest.updateSubscriber(requestId,customer_id, metas);
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

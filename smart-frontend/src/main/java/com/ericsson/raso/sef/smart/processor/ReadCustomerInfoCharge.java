package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.AccountInfo;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.commons.SmartModel;
import com.ericsson.raso.sef.smart.commons.SmartServiceHelper;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.ReadCustomerInfoChargeRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class ReadCustomerInfoCharge implements Processor {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		ReadCustomerInfoChargeRequest request = (ReadCustomerInfoChargeRequest) exchange.getIn().getBody();

		IConfig config = SefCoreServiceResolver.getConfigService();
		String chargeOffer = config.getValue("GLOBAL", "readCustomerInfoChargeOffer");
		//String chargeOffer = SmartContext.getProperty("readCustomerInfoChargeOffer");
		if(chargeOffer == null) {
			log.warn("Customer charge offer is null. Create a offer with name readCustomerInfoChargeOffer to charge the customer.");
		}

		SubscriberValidationProcessor.process(request.getCustomerId());
		//SubscriptionManagement subscriptionManagement = SmartContext.getSubscriptionManagement();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta(SmartConstants.REQUEST_ID, requestId));
		metas.add(new Meta(SmartConstants.SERVICE_IDENTIFIER, SmartConstants.FLEXIBLE_SERVICE_IDENTIFIER));
		metas.add(new Meta(Constants.CHANNEL_NAME, request.getChannel()));
		metas.add(new Meta(Constants.EX_DATA1, String.valueOf(request.getMessageId())));

			//subscriptionManagement.purchaseProduct(request.getCustomerId(), chargeOffer, metas);
		
			purchase(requestId,chargeOffer,request.getCustomerId(),metas);
			exchange.getOut().setBody(readAccountInfo(request.getCustomerId(),request.isTransactional()));
		

	}

	private PurchaseResponse purchase(String requestId,String packaze, String customerId,	List<Meta> metas )
	{
	
		ISubscriptionRequest iSubscriptionRequest = SmartServiceResolver
				.getSubscriptionRequest();

		PurchaseResponse purchaseResponse = new PurchaseResponse();
		RequestCorrelationStore.put(requestId, purchaseResponse);

		iSubscriptionRequest.purchase(requestId, packaze, customerId, false,
				metas);

		log.info("Invoking purchasebe on tx-engine subscribtion interface");

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		log.info("Check if response received for purchase response");
		PurchaseResponse purRsp = (PurchaseResponse) RequestCorrelationStore
				.remove(requestId);

	return purRsp;
	}
	
	private CommandResponseData readAccountInfo(String msisdn,boolean isTransactional) throws SmException {
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
		operation.setName("Read");
		operation.setModifier("CustomerInfoCharge");
		operationResult.getOperation().add(operation);
		ParameterList parameterList = new ParameterList();
		operation.setParameterList(parameterList);
		
		//SmartCommonService smartCommonService = SmartContext.getSmartCommonService();
		//SmartModel smartModel = smartCommonService.readSubscriberAccounts(msisdn);
		
		SmartModel smartModel=SmartServiceHelper.readSubscriberAccounts(msisdn);
		List<AccountInfo> subscriberAccInfos = smartModel.getAccountInfos();
		
		List<AccountInfo> accountInfos = new ArrayList<AccountInfo>();
		List<AccountInfo> subscriptionsInfos = new ArrayList<AccountInfo>();
		for (AccountInfo info : subscriberAccInfos) {
			if(info.getDaId() == SmartConstants.UNLI_DA_ID || info.getOfferId() >= SmartConstants.UNLI_OFFER_START_ID) {
				subscriptionsInfos.add(info);
			} else {
				accountInfos.add(info);
			}
		}
		
		StringParameter accounts = new StringParameter();
		accounts.setName("Accounts");
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(accounts);
		accounts.setValue(buildBalance(accountInfos));
		
		StringParameter subscriptions = new StringParameter();
		subscriptions.setName("Subscriptions");
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(subscriptions);
		subscriptions.setValue(buildBalance(subscriptionsInfos));
		
		return responseData;
	}

	
	private String buildBalance(List<AccountInfo> accountInfos) {
		StringBuilder accountsBuilder = new StringBuilder();
		for (int i = 0; i < accountInfos.size(); i++) {
			AccountInfo accountInfo = accountInfos.get(i);

			long balance = accountInfo.getBalance();
			long confec= getConversionFector(""+accountInfo.getOfferId());
			
			balance = accountInfo.getBalance()/confec;
			
			if(accountInfo.getDaId() == SmartConstants.UNLI_DA_ID || accountInfo.getOfferId() >= SmartConstants.UNLI_OFFER_START_ID) {
				accountsBuilder.append(accountInfo.getName() + ";" + accountInfo.getExpiryDate("yyyy-MM-dd HH:mm:ss"));
			} else {
				accountsBuilder.append(accountInfo.getName() + ";" + balance + ";" + accountInfo.getExpiryDate("yyyy-MM-dd HH:mm:ss"));
			}
			
			if(i < accountInfos.size() -1) {
				accountsBuilder.append("|");
			}
		}
		return accountsBuilder.toString();
	}

	private long getConversionFector(String offerId){

		long conFec =1;
		//IConfig config = SmCoreContext.getConfig();
		//Properties walletMapping = config.properties(IConfig.GLOBAL_COMPONENT, "walletMapping");
		
		String walletName = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", offerId.trim());
		//String walletName = name;
		String conversionFactor = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletConversionFactor", walletName);
		
		if(conversionFactor!=null){
			conFec= Long.parseLong(conversionFactor);
		}
	/*	
		Properties offerConvMapping = config.properties(IConfig.GLOBAL_COMPONENT, SmConstants.OfferConversionFector);
		//String walletName = walletMapping.getProperty(offerId.trim());
		if(walletName!=null){
			String conv = offerConvMapping.getProperty(walletName);
			if(conv!=null){
				conFec= Long.parseLong(conv);
			}
		}*/
		return conFec;
	}
	
}
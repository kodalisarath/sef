package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
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
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.UnSubscribePackageItemRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class UnsubscribePackageItem implements Processor {
	private static final Logger log = LoggerFactory
			.getLogger(UnsubscribePackageItem.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		UnSubscribePackageItemRequest request = (UnSubscribePackageItemRequest) exchange
				.getIn().getBody();

		String requestId = RequestContextLocalStore.get().getRequestId();

		SubscriberValidationProcessor.process(request.getCustomerId());

		if (isWelcomePack(request.getPackaze())) {
			unInstallWelcomePack(requestId, request.getCustomerId(),
					request.getAccessKey(), request.getPackaze(),
					String.valueOf(request.getMessageId()));
		} else {
			unsubscribePackage(requestId, request.getCustomerId(),
					request.getPackaze());
		}

		CommandResponseData responseData = createResponse(request.getUsecase()
				.getOperation(), request.getUsecase().getModifier(),
				request.isTransactional());

		exchange.getOut().setBody(responseData);
	}

	private static boolean isWelcomePack(String packaze) {
		IConfig config = SefCoreServiceResolver.getConfigService();
		if (config.getValue("GLOBAL", packaze) != null)
			return true;
		else
			return false;

	}

	private void unsubscribePackage(String requestId, String customerId,
			String packaze) {

		// SubscriptionManagement subscriptionManagement =
		// SmartContext.getSubscriptionManagement();
		List<Meta> metas = new ArrayList<Meta>();

		metas.add(new Meta(SmartConstants.REQUEST_ID, requestId));
		metas.add(new Meta(SmartConstants.CHANNEL_NAME,
				SmartConstants.IL_CHANNEL));
		metas.add(new Meta(SmartConstants.EX_DATA3, SmartConstants.IL_CHANNEL));
		metas.add(new Meta(SmartConstants.EX_DATA1, packaze));
		metas.add(new Meta(SmartConstants.EX_DATA2, SmartConstants.IL_CHANNEL
				+ "|Reversal|||" + customerId + "|"));
		metas.add(new Meta(SmartConstants.USECASE, "reversal"));

		// subscriptionManagement.purchaseProduct(customerId, packaze, metas);

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

	}

	private void unInstallWelcomePack(String requestId, String customerId,
			String accessKey, String packaze, String messageId)
			throws SmException {

		SubscriberInfo subscriberinfo = readSubscriber(requestId, customerId,
				null);

		if (!ContractState.PREACTIVE.name().equals(
				subscriberinfo.getLocalState())) {

			log.error("Subscriber:"
					+ customerId
					+ "is not in preactive stage. Opeartion can not be performed");
			throw ExceptionUtil.toSmException(ErrorCode.internalServerError);
		}
		String currentPackage = subscriberinfo.getMetas().get("package");
		if (currentPackage == null || !currentPackage.equalsIgnoreCase(packaze)) {
			throw new SmException(
					ErrorCode.invalidSubscriberStateForRequiredPackageUseCaseOperation);
		}

		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("accessKey", accessKey));
		metas.add(new Meta("package", "initialSC"));
		metas.add(new Meta("currentPackage", currentPackage));
		metas.add(new Meta("messageId", messageId));
		metas.add(new Meta("federation-profile", "preloadUnsubscribe"));

		// subscriberManagement.updateSubscriber(customerId, metas);

		updateSubscriber(requestId, customerId, metas);

	}

	private CommandResponseData createResponse(String operationName,
			String modifier, boolean isTransactional) {

		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);

		Operation operation = new Operation();
		operation.setName(operationName);
		operation.setModifier(modifier);

		OperationResult operationResult = new OperationResult();
		operationResult.getOperation().add(operation);

		if (isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}
		return responseData;
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
		log.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;

	}

	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<Meta> metas) {
		log.info("Invoking update subscriber on tx-engine subscriber interface");
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
		log.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;
	}

}

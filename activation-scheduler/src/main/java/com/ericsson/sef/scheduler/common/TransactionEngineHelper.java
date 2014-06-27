package com.ericsson.sef.scheduler.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.subscription.response.SubscriptionEventResponse;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.ericsson.sef.scheduler.ErrorCode;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.hazelcast.core.ISemaphore;

public abstract class TransactionEngineHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(TransactionEngineHelper.class);

	public static SubscriberInfo getSubscriberInfo(String msisdn)
			throws SmException {

		logger.debug("Entering TransactionEngineHelper.....getSubscriberInfo ");
		// String requestId = RequestContextLocalStore.get().getRequestId();
		String requestId = UniqueIdGenerator.generateId();
		logger.debug("Generated TransactionEngineHelper Request ID..... "
				+ requestId);
		SubscriberInfo subscriberInfo = new SubscriberInfo();

		/*
		 * List<Meta> metas = new ArrayList<Meta>(); Meta meta = new Meta();
		 * meta.setKey("READ_SUBSCRIBER");
		 * meta.setValue("PARTIAL_READ_SUBSCRIBER"); metas.add(meta);
		 */logger.debug("Entering SchedulerServiceHelper.....");

		ISubscriberRequest subscriberRequest = SchedulerContext
				.getBean(ISubscriberRequest.class);

		String correlationId = subscriberRequest.readSubscriber(requestId,
				msisdn, null);
		SubscriberResponseStore.put(correlationId, subscriberInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :" + e.getMessage());
		}
		semaphore.destroy();
		logger.debug("Awake from sleep.. going to check subscriber response in store with id: "
				+ correlationId);

		subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.get(correlationId);

		return subscriberInfo;
	}

	public static PurchaseResponse purchase(String offerId, String subscriberId,
			List<Meta> metas) throws SmException {
		
		logger.debug("Entering TransactionEngineHelper.....purchase ");
		ISubscriptionRequest iSubscriptionRequest = SchedulerContext
				.getSubscriptionRequest();
		String requestId = UniqueIdGenerator.generateId();
		String resultId = iSubscriptionRequest.purchase(requestId, offerId,
				subscriberId, true, metas);
		PurchaseResponse purchaseResponse = null;
		logger.debug("Got past event class....");
		RequestCorrelationStore.put(resultId, purchaseResponse);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);

		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :" + e.getMessage());
		}
		semaphore.destroy();

		logger.debug("Awake from sleep.. going to check response in store with id: "
				+ resultId);

		purchaseResponse = (PurchaseResponse) RequestCorrelationStore
				.remove(requestId);

		logger.debug("PurchaseResponse recieved here is " + purchaseResponse);
		if (purchaseResponse == null) {
			logger.debug("No response arrived???");
			throw new SmException(ErrorCode.internalServerError);
		}

		return purchaseResponse;

	}
	
	
	public static SubscriptionEventResponse renew(String subscriptionId,
			List<Meta> metas) throws SmException {
		logger.debug("Entering TransactionEngineHelper.....renew ");
		ISubscriptionRequest iSubscriptionRequest = SchedulerContext
				.getSubscriptionRequest();
		String requestId = UniqueIdGenerator.generateId();
		String resultId = iSubscriptionRequest.renew(requestId, subscriptionId, true, metas);
		SubscriptionEventResponse subscriptionEventResponse = null;
		logger.debug("Got past event class....");
		RequestCorrelationStore.put(resultId, subscriptionEventResponse);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);

		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :" + e.getMessage());
		}
		semaphore.destroy();

		logger.debug("Awake from sleep.. going to check response in store with id: "
				+ resultId);

		subscriptionEventResponse = (SubscriptionEventResponse) RequestCorrelationStore
				.remove(requestId);

		logger.debug("SubscriptionEventResponse recieved here is " + subscriptionEventResponse);
		if (subscriptionEventResponse == null) {
			logger.debug("No response arrived???");
			throw new SmException(ErrorCode.internalServerError);
		}

		return subscriptionEventResponse;

	}

	
	public static SubscriptionEventResponse expiry(String subscriptionId,
			List<Meta> metas) throws SmException {
		logger.debug("Entering TransactionEngineHelper.....expiry ");
		ISubscriptionRequest iSubscriptionRequest = SchedulerContext
				.getSubscriptionRequest();
		String requestId = UniqueIdGenerator.generateId();
		String resultId = iSubscriptionRequest.expiry(requestId, subscriptionId, true, metas);
		SubscriptionEventResponse subscriptionEventResponse = null;
		logger.debug("Got past event class....");
		RequestCorrelationStore.put(resultId, subscriptionEventResponse);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);

		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :" + e.getMessage());
		}
		semaphore.destroy();

		logger.debug("Awake from sleep.. going to check response in store with id: "
				+ resultId);

		subscriptionEventResponse = (SubscriptionEventResponse) RequestCorrelationStore
				.remove(requestId);

		logger.debug("SubscriptionEventResponse recieved here is " + subscriptionEventResponse);
		if (subscriptionEventResponse == null) {
			logger.debug("No response arrived???");
			throw new SmException(ErrorCode.internalServerError);
		}

		return subscriptionEventResponse;

	}

}

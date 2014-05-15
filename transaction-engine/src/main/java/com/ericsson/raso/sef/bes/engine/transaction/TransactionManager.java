package com.ericsson.raso.sef.bes.engine.transaction;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.ericsson.raso.sef.bes.engine.transaction.commands.CreateSubscriber;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.Subscriber;
import com.ericsson.raso.sef.bes.engine.transaction.service.ISubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.service.ISubscriptionRequest;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.UniqueIdGenerator;

public class TransactionManager implements ISubscriberRequest, ISubscriptionRequest {

	IOfferCatalog catalog = null;
	ExecutorService executor = null;
	
	
	public TransactionManager() {
		catalog = ServiceResolver.getOfferCatalog();
		executor = SefCoreServiceResolver.getExecutorService(Constants.USE_CASE_EVAL.name());
	}


	@Override
	public String discoverOffers(String resource) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String discoverOffersForUser(String resource, String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String discoverOfferById(String offerId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String discoverOfferById(String offerId, String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String discoverOfferByFederatedId(String handle, String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getAdviceOfCharge(String offerId, String subscriberId, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String purchase(String offerId, String subscriberId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String terminate(String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String expiry(String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String renew(String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String preExpiry(String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String preRenewal(String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String querySubscription(String subscriptionId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String readSubscriber(String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String readSubscriberMeta(String subscriberId, Set<String> metaNames) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String createSubscriber(Subscriber subscriber) {
		String requestId = UniqueIdGenerator.generateId();
		CreateSubscriber command = new CreateSubscriber(requestId, subscriber);
		executor.submit(command);
		return requestId;
	}


	@Override
	public String updateSubscriber(String subscriberId, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String deleteSubscriber(String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String handleLifeCycle(String subscriberId, String lifeCycleState, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	
}

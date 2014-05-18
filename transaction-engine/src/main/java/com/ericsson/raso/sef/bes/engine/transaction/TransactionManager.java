package com.ericsson.raso.sef.bes.engine.transaction;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.ericsson.raso.sef.bes.engine.transaction.commands.CreateSubscriber;
import com.ericsson.raso.sef.bes.engine.transaction.entities.Subscriber;
import com.ericsson.raso.sef.bes.engine.transaction.service.ISubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.service.ISubscriptionRequest;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class TransactionManager implements ISubscriberRequest, ISubscriptionRequest {

	IOfferCatalog catalog = null;
	ExecutorService executor = null;
	
	
	public TransactionManager() {
		catalog = ServiceResolver.getOfferCatalog();
		executor = SefCoreServiceResolver.getExecutorService(Constants.USE_CASE_EVAL.name());
	}


	@Override
	public String createSubscriber(String requestId, Subscriber subscriber) {
		CreateSubscriber command = new CreateSubscriber(requestId, subscriber);
		executor.submit(command);
		return requestId;
	}


	@Override
	public String discoverOffers(String requestId, String resource) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String discoverOffersForUser(String requestId, String resource, String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String discoverOfferById(String requestId, String offerId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String discoverOfferById(String requestId, String offerId, String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String discoverOfferByFederatedId(String requestId, String handle, String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getAdviceOfCharge(String requestId, String offerId, String subscriberId, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String purchase(String requestId, String offerId, String subscriberId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String terminate(String requestId, String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String expiry(String requestId, String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String renew(String requestId, String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String preExpiry(String requestId, String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String preRenewal(String requestId, String subscriptionId, Boolean override, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String querySubscription(String requestId, String subscriptionId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String readSubscriber(String requestId, String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String readSubscriberMeta(String requestId, String subscriberId, Set<String> metaNames) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String updateSubscriber(String requestId, String subscriberId, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String deleteSubscriber(String requestId, String subscriberId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String handleLifeCycle(String requestId, String subscriberId, String lifeCycleState, Map<String, String> metas) {
		// TODO Auto-generated method stub
		return null;
	}


		
}

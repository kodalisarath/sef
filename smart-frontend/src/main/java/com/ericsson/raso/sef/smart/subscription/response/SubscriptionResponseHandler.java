package com.ericsson.raso.sef.smart.subscription.response;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscription.ISubscriptionResponse;
import com.hazelcast.core.ISemaphore;

public class SubscriptionResponseHandler implements ISubscriptionResponse {
	
	
	private static final Logger logger = LoggerFactory.getLogger(SubscriptionResponseHandler.class);

	@Override
	public void discoverOffers(String requestCorrelator, TransactionStatus fault, Set<Offer> offers) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setOffers(offers);
			RequestCorrelationStore.put(requestCorrelator, response);
		}
		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public void discoverOffersForUser(String requestCorrelator, TransactionStatus fault, Set<Offer> offers) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setOffers(offers);
			RequestCorrelationStore.put(requestCorrelator, response);
		}

		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public void discoverOfferById(String requestCorrelator, TransactionStatus fault, Offer offer) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setOffer(offer);
			RequestCorrelationStore.put(requestCorrelator, response);
		}

		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public void discoverOfferByFederatedId(String requestCorrelator, TransactionStatus fault, Offer offer) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		DiscoveryResponse response = (DiscoveryResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setOffer(offer);
			response.setFault(fault);
			RequestCorrelationStore.put(requestCorrelator, response);
		logger.debug("Successfully set the response object in response store: " + response);
		}

		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
		logger.debug("Released the semaphore lock waiting on: " + requestCorrelator);
	}

	@Override
	public void getAdviceOfCharge(String requestCorrelator, TransactionStatus fault, long priceAmount, String iso4217) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setMonetaryAmount(priceAmount);
			response.setCurrency(iso4217);
			RequestCorrelationStore.put(requestCorrelator, response);
		}

		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public void purchase(String requestCorrelator, TransactionStatus fault,	String subscriptionId, List<Product> products, List<Meta> billingMetas) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		PurchaseResponse response = (PurchaseResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setBillingMetas(billingMetas);
			response.setProducts(products);
			response.setSubscriptionId(subscriptionId);
			response.setFault(fault);
			RequestCorrelationStore.put(requestCorrelator, response);
		}

		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}
	
	@Override
	public void terminate(String requestCorrelator, TransactionStatus fault, Boolean result) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setResult(result);
			RequestCorrelationStore.put(requestCorrelator, response);
		}

		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public void expiry(String requestCorrelator, TransactionStatus fault, Boolean result) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setResult(result);
			RequestCorrelationStore.put(requestCorrelator, response);
		}
		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public void renew(String requestCorrelator, TransactionStatus fault, Boolean result) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setResult(result);
			RequestCorrelationStore.put(requestCorrelator, response);
		}

		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	@Override
	public String preExpiry(String requestCorrelator, TransactionStatus fault, Boolean result) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setResult(result);
			RequestCorrelationStore.put(requestCorrelator, response);
		}
		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
		return "true"; //TODO: what to do? too late for fixing this mistake... must fix in next release
	}

	@Override
	public String preRenewal(String requestCorrelator, TransactionStatus fault, Boolean result) {
	logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setResult(result);
			RequestCorrelationStore.put(requestCorrelator, response);
		}
		
		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
		return "true"; //TODO: what to do? too late for fixing this mistake... must fix in next release
	}

	@Override
	public String querySubscription(String requestCorrelator, TransactionStatus fault, Offer subscription) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setSubscription(subscription);
			RequestCorrelationStore.put(requestCorrelator, response);
		}
		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
		return "true"; //TODO: what to do? too late for fixing this mistake... must fix in next release
	}

	@Override
	public void discoverOfferForUserById(String requestCorrelator, TransactionStatus fault, String subscriber, Offer offer) {
		logger.debug("Fetching relavant response from correlation for updating the received response");
		
		//Step 1: Place the response in a location where the original node can access
		SubscriptionEventResponse response = (SubscriptionEventResponse) RequestCorrelationStore.get(requestCorrelator);
		if ( response != null){
			response.setFault(fault);
			response.setOffer(offer);
			RequestCorrelationStore.put(requestCorrelator, response);
		}
		//Step 2: Trigger the original node that is waiting for response
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		semaphore.release();
	}

	
	

}

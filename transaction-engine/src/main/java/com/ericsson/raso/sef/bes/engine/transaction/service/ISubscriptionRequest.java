package com.ericsson.raso.sef.bes.engine.transaction.service;

import java.util.Map;

public interface ISubscriptionRequest {
	
	public abstract String discoverOffers(String requestId, String resource);
	
	public abstract String discoverOffersForUser(String requestId, String resource, String subscriberId);
	
	public abstract String discoverOfferById(String requestId, String offerId);
	
	public abstract String discoverOfferById(String requestId, String offerId, String subscriberId);
	
	public abstract String discoverOfferByFederatedId(String requestId, String handle, String subscriberId);
	
	public abstract String getAdviceOfCharge(String requestId, String offerId, String subscriberId, Map<String, String> metas);
	
	public abstract String purchase(String requestId, String offerId, String subscriberId, Boolean override, Map<String, String> metas);
	
	public abstract String terminate(String requestId, String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String expiry(String requestId, String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String renew(String requestId, String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String preExpiry(String requestId, String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String preRenewal(String requestId, String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String querySubscription(String requestId, String subscriptionId);
}

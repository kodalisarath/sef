package com.ericsson.sef.bes.api.subscription;

import java.util.Map;

public interface ISubscriptionRequest {
	
	public abstract String discoverOffers(String resource);
	
	public abstract String discoverOffersForUser(String resource, String subscriberId);
	
	public abstract String discoverOfferById(String offerId);
	
	public abstract String discoverOfferById(String offerId, String subscriberId);
	
	public abstract String discoverOfferByFederatedId(String handle, String subscriberId);
	
	public abstract String getAdviceOfCharge(String offerId, String subscriberId, Map<String, String> metas);
	
	public abstract String purchase(String offerId, String subscriberId, Boolean override, Map<String, String> metas);
	
	public abstract String terminate(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String expiry(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String renew(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String preExpiry(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String preRenewal(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public abstract String querySubscription(String subscriptionId);
}

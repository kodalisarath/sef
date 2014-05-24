package com.ericsson.sef.bes.api.subscription;

import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
@SuppressWarnings("restriction")
@WebService
public interface ISubscriptionRequest {
	
	public @WebMethod abstract String discoverOffers(String resource);
	
	public @WebMethod abstract String discoverOffersForUser(String resource, String subscriberId);
	
	public @WebMethod abstract String discoverOfferById(String offerId);
	
	public @WebMethod abstract String discoverOfferById(String offerId, String subscriberId);
	
	public @WebMethod abstract String discoverOfferByFederatedId(String handle, String subscriberId);
	
	public @WebMethod abstract String getAdviceOfCharge(String offerId, String subscriberId, Map<String, String> metas);
	
	public @WebMethod abstract String purchase(String offerId, String subscriberId, Boolean override, Map<String, String> metas);
	
	public @WebMethod abstract String terminate(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public @WebMethod abstract String expiry(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public @WebMethod abstract String renew(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public @WebMethod abstract String preExpiry(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public @WebMethod abstract String preRenewal(String subscriptionId, Boolean override, Map<String, String> metas);
	
	public @WebMethod abstract String querySubscription(String subscriptionId);
}
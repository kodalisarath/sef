package com.ericsson.raso.sef.bes.engine.transaction.service;

import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
@SuppressWarnings("restriction")
@WebService
public interface ISubscriptionRequest {
	
	public @WebMethod abstract String discoverOffers(String requestId, String resource);
	
	public @WebMethod abstract String discoverOffersForUser(String requestId, String resource, String subscriberId);
	
	public @WebMethod abstract String discoverOfferById(String requestId, String offerId);
	
	public @WebMethod abstract String discoverOfferById(String requestId, String offerId, String subscriberId);
	
	public @WebMethod abstract String discoverOfferByFederatedId(String requestId, String handle, String subscriberId);
	
	public @WebMethod abstract String getAdviceOfCharge(String requestId, String offerId, String subscriberId, Map<String, String> metas);
	
	public @WebMethod abstract String purchase(String requestId, String offerId, String subscriberId, Boolean override, Map<String, Object> metas);
	
	public @WebMethod abstract String terminate(String requestId, String subscriptionId, Boolean override, Map<String, Object> metas);
	
	public @WebMethod abstract String expiry(String requestId, String subscriptionId, Boolean override, Map<String, Object> metas);
	
	public @WebMethod abstract String renew(String requestId, String subscriptionId, Boolean override, Map<String, Object> metas);
	
	public @WebMethod abstract String preExpiry(String requestId, String subscriptionId, Boolean override, Map<String, Object> metas);
	
	public @WebMethod abstract String preRenewal(String requestId, String subscriptionId, Boolean override, Map<String, Object> metas);
	
	public @WebMethod abstract String querySubscription(String requestId, String subscriptionId);
}
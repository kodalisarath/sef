package com.ericsson.sef.bes.api.subscription;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Meta;

@WebService
public interface ISubscriptionRequest {
	
	public @WebMethod abstract String discoverOffers(String requestId, String resource);
	
	public @WebMethod abstract String discoverOffersForUser(String requestId, String resource, String subscriberId);
	
	public @WebMethod abstract String discoverOfferById(String requestId, String offerId);
	
	public @WebMethod abstract String discoverOfferForUserById(String requestId, String offerId, String subscriberId);
	
	public @WebMethod abstract String discoverOfferByFederatedId(String requestId, String handle, String subscriberId);
	
	public @WebMethod abstract String getAdviceOfCharge(String requestId, String offerId, String subscriberId, List<Meta> metas);
	
	public @WebMethod abstract String purchase(String requestId, String offerId, String subscriberId, Boolean override, List<Meta> metas);
	
	public @WebMethod abstract String terminate(String requestId, String subscriptionId, Boolean override, List<Meta> metas);
	
	public @WebMethod abstract String expiry(String requestId, String subscriptionId, Boolean override, List<Meta> metas);
	
	public @WebMethod abstract String renew(String requestId, String subscriptionId, Boolean override, List<Meta> metas);
	
	public @WebMethod abstract String preExpiry(String requestId, String subscriptionId, Boolean override, List<Meta> metas);
	
	public @WebMethod abstract String preRenewal(String requestId, String subscriptionId, Boolean override, List<Meta> metas);
	
	public @WebMethod abstract String querySubscription(String requestId, String subscriptionId);
}
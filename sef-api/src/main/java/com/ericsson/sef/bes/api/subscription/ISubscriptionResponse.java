package com.ericsson.sef.bes.api.subscription;

import java.util.Map;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.TransactionException;
@SuppressWarnings("restriction")
@WebService
public interface ISubscriptionResponse {
	
	public @WebMethod abstract void discoverOffers(String requestCorrelator, TransactionException fault, Set<Offer> offers);
	
	public @WebMethod abstract void discoverOffersForUser(String requestCorrelator, TransactionException fault, Set<Offer> offers);
	
	public @WebMethod abstract void discoverOfferById(String requestCorrelator, TransactionException fault, Offer offer);
	
	public @WebMethod abstract void discoverOfferById(String requestCorrelator, TransactionException fault, String subscriber, Offer offer);
	
	public @WebMethod abstract void discoverOfferByFederatedId(String requestCorrelator, TransactionException fault, Offer offer);
	
	
	
	public @WebMethod abstract void getAdviceOfCharge(String requestCorrelator, TransactionException fault, long priceAmount, String iso4217);
	
	public @WebMethod abstract void purchase(String requestCorrelator, TransactionException fault, String subscriptionId, Map<String, String> billingMetas);
	
	public @WebMethod abstract void terminate(String requestCorrelator, TransactionException fault, Boolean result);
	
	public @WebMethod abstract void expiry(String requestCorrelator, TransactionException fault, Boolean result);
	
	public @WebMethod abstract void renew(String requestCorrelator, TransactionException fault, Boolean result);
	
	public @WebMethod abstract String preExpiry(String requestCorrelator, TransactionException fault, Boolean result);
	
	public @WebMethod abstract String preRenewal(String requestCorrelator, TransactionException fault, Boolean result);
	
	public @WebMethod abstract String querySubscription(String requestCorrelator, TransactionException fault, Offer subscription);

}
package com.ericsson.raso.sef.bes.engine.transaction.service;

import java.util.Map;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;

public interface ISubscriptionResponse {
	
//	public abstract void discoverOffers(String requestCorrelator, TransactionException fault, Set<Offer> offers);
//	
//	public abstract void discoverOffersForUser(String requestCorrelator, TransactionException fault, Set<Offer> offers);
//	
//	public abstract void discoverOfferById(String requestCorrelator, TransactionException fault, Offer offer);
//	
//	public abstract void discoverOfferById(String requestCorrelator, TransactionException fault, String subscriber, Offer offer);
//	
//	public abstract void discoverOfferByFederatedId(String requestCorrelator, TransactionException fault, Offer offer);
//	
	
	
	public abstract void getAdviceOfCharge(String requestCorrelator, TransactionException fault, long priceAmount, String iso4217);
	
	public abstract void purchase(String requestCorrelator, TransactionException fault, String subscriptionId, Map<String, String> billingMetas);
	
	public abstract void terminate(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract void expiry(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract void renew(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract String preExpiry(String requestCorrelator, TransactionException fault, Boolean result);
	
	public abstract String preRenewal(String requestCorrelator, TransactionException fault, Boolean result);
	
	//public abstract String querySubscription(String requestCorrelator, TransactionException fault, Offer subscription);

}

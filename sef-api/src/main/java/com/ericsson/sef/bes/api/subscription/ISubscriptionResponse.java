package com.ericsson.sef.bes.api.subscription;

import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;


@WebService
public interface ISubscriptionResponse {
	
	public @WebMethod abstract void discoverOffers(String requestCorrelator, TransactionStatus fault, Set<Offer> offers);
	
	public @WebMethod abstract void discoverOffersForUser(String requestCorrelator, TransactionStatus fault, Set<Offer> offers);
	
	public @WebMethod abstract void discoverOfferById(String requestCorrelator, TransactionStatus fault, Offer offer);
	
	public @WebMethod abstract void discoverOfferForUserById(String requestCorrelator, TransactionStatus fault, String subscriber, Offer offer);
	
	public @WebMethod abstract void discoverOfferByFederatedId(String requestCorrelator, TransactionStatus fault, Offer offer);
	
	
	
	public @WebMethod abstract void getAdviceOfCharge(String requestCorrelator, TransactionStatus fault, long priceAmount, String iso4217);
	
	public @WebMethod abstract void purchase(String requestCorrelator, TransactionStatus fault, String subscriptionId, List<Product> products,List<Meta> billingMetas);
	
	public @WebMethod abstract void terminate(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public @WebMethod abstract void expiry(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public @WebMethod abstract void renew(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public @WebMethod abstract String preExpiry(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public @WebMethod abstract String preRenewal(String requestCorrelator, TransactionStatus fault, Boolean result);
	
	public @WebMethod abstract String querySubscription(String requestCorrelator, TransactionStatus fault, Offer subscription);

}
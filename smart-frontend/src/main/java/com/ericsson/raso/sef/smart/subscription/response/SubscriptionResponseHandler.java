package com.ericsson.raso.sef.smart.subscription.response;

import java.util.List;
import java.util.Set;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscription.ISubscriptionResponse;

public class SubscriptionResponseHandler implements ISubscriptionResponse {

	@Override
	public void discoverOffers(String requestCorrelator,
			TransactionStatus fault, Set<Offer> offers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverOffersForUser(String requestCorrelator,
			TransactionStatus fault, Set<Offer> offers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverOfferById(String requestCorrelator,
			TransactionStatus fault, Offer offer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverOfferByFederatedId(String requestCorrelator,
			TransactionStatus fault, Offer offer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAdviceOfCharge(String requestCorrelator,
			TransactionStatus fault, long priceAmount, String iso4217) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void purchase(String requestCorrelator, TransactionStatus fault,
			String subscriptionId, List<Product> products,
			List<Meta> billingMetas) {
		PurchaseResponse response = (PurchaseResponse) RequestCorrelationStore.get(requestCorrelator);
		synchronized (response) {
			response.setBillingMetas(billingMetas);
			response.setProducts(products);
			response.setSubscriptionId(subscriptionId);
			response.setFault(fault);
			response.notify();
		}
	}
	
	/*@Override
	public void purchase(String requestCorrelator, TransactionException fault,String subscriptionId,List<Meta> billingMetas) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void terminate(String requestCorrelator, TransactionStatus fault,
			Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void expiry(String requestCorrelator, TransactionStatus fault,
			Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renew(String requestCorrelator, TransactionStatus fault,
			Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String preExpiry(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preRenewal(String requestCorrelator,
			TransactionStatus fault, Boolean result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String querySubscription(String requestCorrelator,
			TransactionStatus fault, Offer subscription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void discoverOfferForUserById(String requestCorrelator,
			TransactionStatus fault, String subscriber, Offer offer) {
		// TODO Auto-generated method stub
		
	}

	
	

}

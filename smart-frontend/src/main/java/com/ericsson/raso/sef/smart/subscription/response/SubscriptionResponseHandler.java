package com.ericsson.raso.sef.smart.subscription.response;

import java.util.List;
import java.util.Set;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionException;
import com.ericsson.sef.bes.api.subscription.ISubscriptionResponse;

public class SubscriptionResponseHandler implements ISubscriptionResponse {

	@Override
	public void discoverOffers(String requestCorrelator,
			TransactionException fault, Set<Offer> offers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverOffersForUser(String requestCorrelator,
			TransactionException fault, Set<Offer> offers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverOfferById(String requestCorrelator,
			TransactionException fault, Offer offer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverOfferByFederatedId(String requestCorrelator,
			TransactionException fault, Offer offer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAdviceOfCharge(String requestCorrelator,
			TransactionException fault, long priceAmount, String iso4217) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void purchase(String requestCorrelator, TransactionException fault,
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
	public void terminate(String requestCorrelator, TransactionException fault,
			Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void expiry(String requestCorrelator, TransactionException fault,
			Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renew(String requestCorrelator, TransactionException fault,
			Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String preExpiry(String requestCorrelator,
			TransactionException fault, Boolean result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preRenewal(String requestCorrelator,
			TransactionException fault, Boolean result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String querySubscription(String requestCorrelator,
			TransactionException fault, Offer subscription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void discoverOfferForUserById(String requestCorrelator,
			TransactionException fault, String subscriber, Offer offer) {
		// TODO Auto-generated method stub
		
	}

	
	

}

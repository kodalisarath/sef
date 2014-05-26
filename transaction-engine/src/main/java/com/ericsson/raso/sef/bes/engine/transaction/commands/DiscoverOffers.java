package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.Set;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.entities.DiscoverOffersRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.DiscoverOffersResponse;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.sef.bes.api.subscription.ISubscriptionResponse;


public class DiscoverOffers extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;

	public DiscoverOffers(String requestId, String resource) {
		super(requestId, new DiscoverOffersRequest(requestId, resource));
		this.setResponse(new DiscoverOffersResponse(requestId));
	}

	@Override
	public Void execute() throws TransactionException {
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Set<Offer> prodcatOffers = catalog.getOffersByResource(((DiscoverOffersRequest)this.getRequest()).getResource());
		
		Set<com.ericsson.sef.bes.api.entities.Offer> resultOffers = new TreeSet<com.ericsson.sef.bes.api.entities.Offer>();
		for (Offer tempOffer: prodcatOffers) {
			resultOffers.add(TransactionServiceHelper.getApiEntity(tempOffer));
		}
		
		
		((DiscoverOffersResponse)this.getResponse()).setResult(resultOffers);
		
		this.sendResponse();
		
		return null;
	}

	
	@Override
	public void sendResponse() {
		//TODO: implement this logic
		/*
		 * 1. when this method is called, it means that Orchestration Manager has executed all steps in the transaction. Either a response or
		 * exception is available.
		 * 
		 * 2. The response will most likely be results/ responses/ exceptions from atomic steps in the transaction. This must be packed into
		 * the response pojo structure pertinent to method signature of the response interface.
		 * 
		 * 3. once the response pojo entity is packed, the client for response interface must be invoked. the assumption is that response
		 * interface will notify the right JVM waiting for this response thru a Object.wait
		 */
		
		ISubscriptionResponse subscriptionClient = ServiceResolver.getSubscriptionResponseClient();
		if (subscriptionClient != null) {
			subscriptionClient.discoverOffers(this.getRequestId(), ((DiscoverOffersResponse)this.getResponse()).getReturnFault(), ((DiscoverOffersResponse)this.getResponse()).getResult());
			//TODO: This error is because the api package is not yet refactored to align with the namespace com.ericsson.raso.sef... Fix it!!
		}
		
	}
	
	

}

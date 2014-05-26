package com.ericsson.raso.sef.bes.engine.transaction.commands;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferForUserRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferForUserResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferResponse;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.sef.bes.api.subscription.ISubscriptionResponse;


public class FetchOfferForUser extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;

	public FetchOfferForUser(String requestId, String offerId, String subscriberId) {
		super(requestId, new FetchOfferForUserRequest(requestId, offerId, subscriberId));
		this.setResponse(new FetchOfferForUserResponse(requestId));
	}

	@Override
	public Void execute() throws TransactionException {
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Offer prodcatOffer = catalog.getOfferById(((FetchOfferForUserRequest)this.getRequest()).getOfferId());
		
		try {
			prodcatOffer.execute(((FetchOfferForUserRequest)this.getRequest()).getSusbcriberId(), SubscriptionLifeCycleEvent.PURCHASE, false, null);
			com.ericsson.sef.bes.api.entities.Offer resultantOffer = TransactionServiceHelper.getApiEntity(prodcatOffer);
			((FetchOfferForUserResponse)this.getResponse()).setResult(resultantOffer);
			((FetchOfferForUserResponse)this.getResponse()).setSubscriberId(((FetchOfferForUserRequest)this.getRequest()).getSusbcriberId());
		} catch (CatalogException e) {
			((FetchOfferForUserResponse)this.getResponse()).setReturnFault(new TransactionException(this.getRequestId(), "Offer not available for user", e));
				
		}
		
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
			subscriptionClient.discoverOfferById(this.getRequestId(), ((FetchOfferForUserResponse)this.getResponse()).getReturnFault(), ((FetchOfferForUserResponse)this.getResponse()).getSubscriberId(), ((FetchOfferForUserResponse)this.getResponse()).getResult());
			//TODO: This error is because the api package is not yet refactored to align with the namespace com.ericsson.raso.sef... Fix it!!
		}

	}
	
	

}

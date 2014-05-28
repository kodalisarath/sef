package com.ericsson.raso.sef.bes.engine.transaction.commands;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferByHandleForUserRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferByHandleForUserResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferForUserRequest;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;


public class FetchOfferByHandleForUser extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;

	public FetchOfferByHandleForUser(String requestId, String handle, String subscriberId) {
		super(requestId, new FetchOfferByHandleForUserRequest(requestId, handle, subscriberId));
		this.setResponse(new FetchOfferByHandleForUserResponse(requestId));
	}

	@Override
	public Boolean execute() throws TransactionException {
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Offer prodcatOffer = catalog.getOfferByExternalHandle(((FetchOfferByHandleForUserRequest)this.getRequest()).getHandle());
		
		try {
			prodcatOffer.execute(((FetchOfferForUserRequest)this.getRequest()).getSusbcriberId(), SubscriptionLifeCycleEvent.PURCHASE, false, null);
		//	com.ericsson.sef.bes.api.entities.Offer resultantOffer = TransactionServiceHelper.getApiEntity(prodcatOffer);
			((FetchOfferByHandleForUserResponse)this.getResponse()).setSubscriberId(((FetchOfferForUserRequest)this.getRequest()).getSusbcriberId());
			//((FetchOfferByHandleForUserResponse)this.getResponse()).setResult(resultantOffer);
			
		} catch (CatalogException e) {
			((FetchOfferByHandleForUserResponse)this.getResponse()).setReturnFault(new TransactionException(this.getRequestId(), "Offer not available for user", e));
		}
		
		
		return true;
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
		
//		ISubscriptionResponse subscriptionClient = ServiceResolver.getSubscriptionResponseClient();
//		if (subscriptionClient != null) {
//			subscriptionClient.discoverOfferByFederatedId(this.getRequestId(), ((FetchOfferByHandleForUserResponse)this.getResponse()).getReturnFault(), ((FetchOfferByHandleForUserResponse)this.getResponse()).getSubscriberId(), ((FetchOfferByHandleForUserResponse)this.getResponse()).getResult());
//			//TODO: This error is because the api package is not yet refactored to align with the namespace com.ericsson.raso.sef... Fix it!!
//		}

	}
	
	

}

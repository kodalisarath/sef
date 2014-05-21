package com.ericsson.raso.sef.bes.engine.transaction.commands;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferResponse;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;


public class FetchOffer extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;

	public FetchOffer(String requestId, String offerId) {
		super(requestId, new FetchOfferRequest(requestId, offerId));
		this.setResponse(new FetchOfferResponse(requestId));
	}

	@Override
	public Void execute() throws TransactionException {
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Offer prodcatOffer = catalog.getOfferById(((FetchOfferRequest)this.getRequest()).getOfferId());
		com.ericsson.raso.sef.bes.engine.transaction.entities.Offer resultantOffer = new com.ericsson.raso.sef.bes.engine.transaction.entities.Offer(prodcatOffer);
		((FetchOfferResponse)this.getResponse()).setResult(resultantOffer);
		
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
	}
	
	

}

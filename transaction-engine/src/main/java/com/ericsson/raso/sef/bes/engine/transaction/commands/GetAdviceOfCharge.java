package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.Map;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferForUserRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferForUserResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.GetAdviceOfChargeRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.GetAdviceOfChargeResponse;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;


public class GetAdviceOfCharge extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;

	public GetAdviceOfCharge(String requestId, String offerId, String subscriberId, Map<String, String> metas) {
		super(requestId, new GetAdviceOfChargeRequest(requestId, offerId, subscriberId, metas));
		this.setResponse(new GetAdviceOfChargeResponse(requestId));
	}

	@Override
	public Boolean execute() throws TransactionException {
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Offer prodcatOffer = catalog.getOfferById(((GetAdviceOfChargeRequest)this.getRequest()).getOfferId());
		
		try {
			prodcatOffer.execute(((GetAdviceOfChargeRequest)this.getRequest()).getSusbcriberId(), SubscriptionLifeCycleEvent.PURCHASE, false, null);
			MonetaryUnit aoc = prodcatOffer.getPrice().getSimpleAdviceOfCharge();
			
			((GetAdviceOfChargeResponse)this.getResponse()).setAmount(aoc.getAmount());
			((GetAdviceOfChargeResponse)this.getResponse()).setIso4217Currency(aoc.getIso4217CurrencyCode());
			
		} catch (CatalogException e) {
			((GetAdviceOfChargeResponse)this.getResponse()).setReturnFault(new TransactionException(this.getRequestId(), "Offer not available for user", e));
				
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
	}
	
	

}

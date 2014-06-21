package com.ericsson.raso.sef.bes.engine.transaction.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferByHandleForUserRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferByHandleForUserResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferForUserRequest;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscription.ISubscriptionResponse;



public class FetchOfferByHandleForUser extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;
	private static final Logger logger = LoggerFactory.getLogger(FetchOfferByHandleForUser.class);
	
	private TransactionStatus status = new TransactionStatus();

	public FetchOfferByHandleForUser(String requestId, String handle, String subscriberId) {
		super(requestId, new FetchOfferByHandleForUserRequest(requestId, handle, subscriberId));
		this.setResponse(new FetchOfferByHandleForUserResponse(requestId));
		logger.debug("Fetching offer for handle: " + handle);
	}

	@Override
	public Boolean execute() throws TransactionException {
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Offer prodcatOffer = catalog.getOfferByExternalHandle(((FetchOfferByHandleForUserRequest)this.getRequest()).getHandle());
		if (prodcatOffer == null) {
			status.setCode(999);
			status.setComponent("txe");
			status.setDescription("invalid event name");
			((FetchOfferByHandleForUserResponse)this.getResponse()).setReturnFault(new TransactionException("txe", new ResponseCode(status.getCode(), status.getDescription())));
			logger.debug("Offer not found for handle: " + ((FetchOfferByHandleForUserRequest)this.getRequest()).getHandle());
			this.sendResponse();
		}
		
		com.ericsson.sef.bes.api.entities.Offer resultantOffer = TransactionServiceHelper.getApiEntity(prodcatOffer);
		logger.debug("Translated to API entity: " + resultantOffer);
		((FetchOfferByHandleForUserResponse)this.getResponse()).setSubscriberId(((FetchOfferByHandleForUserRequest)this.getRequest()).getSusbcriberId());
		((FetchOfferByHandleForUserResponse)this.getResponse()).setResult(resultantOffer);

		status.setCode(0);
		status.setComponent("txe");
		status.setDescription("success");
		((FetchOfferByHandleForUserResponse)this.getResponse()).setReturnFault(null);
		logger.debug("Creating successful response for SMFE...");

		this.sendResponse();
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
		logger.debug("Use case response to be sent...");
		TransactionException e = this.getResponse().getReturnFault();
		if (e != null) {
			status.setComponent(e.getComponent());
			status.setCode(e.getStatusCode().getCode());
			status.setDescription(e.getStatusCode().getMessage());
		}
		
		
		ISubscriptionResponse subscriptionClient = ServiceResolver.getSubscriptionResponseClient();
		if (subscriptionClient != null) {
			logger.debug("Susbcription Client available now... About to trigger response...");
			subscriptionClient.discoverOfferByFederatedId(this.getRequestId(), 
					status,
					((FetchOfferByHandleForUserResponse)this.getResponse()).getResult());
		} else {
			logger.debug("Cannot access client.. This request will timeout in the SMFE", new IllegalStateException("No Client available")); 
		}

	}
	
	

}

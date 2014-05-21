package com.ericsson.raso.sef.bes.engine.transaction.commands;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.QuerySubscriptionRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.QuerySubscriptionResponse;
import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.bes.prodcat.tasks.FetchSubscription;
import com.ericsson.raso.sef.core.FrameworkException;


public class QuerySubscription extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;

	public QuerySubscription(String requestId, String subscriptionId) {
		super(requestId, new QuerySubscriptionRequest(requestId, subscriptionId));
		this.setResponse(new QuerySubscriptionResponse(requestId));
	}

	@Override
	public Void execute() throws TransactionException {
		
		try {
			Subscription subscription = new FetchSubscription(((QuerySubscriptionRequest)this.getRequest()).getSubscriptionId()).execute();
			com.ericsson.raso.sef.bes.engine.transaction.entities.Offer result = new com.ericsson.raso.sef.bes.engine.transaction.entities.Offer(subscription);
			((QuerySubscriptionResponse)this.getResponse()).setResult(result);
		} catch (FrameworkException e) {
			((QuerySubscriptionResponse)this.getResponse()).setReturnFault(new TransactionException(this.getRequestId(), "Unable to fetch subscription", e));
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
	}
	
	

}

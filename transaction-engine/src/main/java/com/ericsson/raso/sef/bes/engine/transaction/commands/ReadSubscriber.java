package com.ericsson.raso.sef.bes.engine.transaction.commands;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.entities.QuerySubscriptionRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.QuerySubscriptionResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.ReadSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.ReadSubscriberResponse;
import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.bes.prodcat.tasks.FetchSubscriber;
import com.ericsson.raso.sef.bes.prodcat.tasks.FetchSubscription;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.db.model.Subscriber;


public class ReadSubscriber extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;

	public ReadSubscriber(String requestId, String subscriberId) {
		super(requestId, new ReadSubscriberRequest(requestId, subscriberId));
		this.setResponse(new ReadSubscriberResponse(requestId));
	}

	@Override
	public Boolean execute() throws TransactionException {
		
		try {
			Subscriber subscriber = new FetchSubscriber(((ReadSubscriberRequest)this.getRequest()).getSubscriberId()).execute();
			com.ericsson.sef.bes.api.entities.Subscriber result = TransactionServiceHelper.getApiEntity(subscriber);
			((ReadSubscriberResponse)this.getResponse()).setSubscriber(result);
		} catch (FrameworkException e) {
			((QuerySubscriptionResponse)this.getResponse()).setReturnFault(new TransactionException(this.getRequestId(), "Unable to fetch subscription", e));
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

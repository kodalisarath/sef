package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberResponse;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.Orchestration;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.OrchestrationManager;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.bes.prodcat.tasks.PersistenceMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.sef.bes.api.entities.Subscriber;

public class CreateSubscriber extends AbstractTransaction {
	private static final long	serialVersionUID	= 8085575039162225609L;
	
	
	public CreateSubscriber(String requestId, Subscriber subscriber) {
		super(requestId, new CreateSubscriberRequest(requestId, subscriber));
		this.setResponse(new CreateSubscriberResponse(requestId));
	}
	
	
	@Override
	public Boolean execute() throws TransactionException {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>(); 
		
		
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity = ((CreateSubscriberRequest)this.getRequest()).persistableEntity();
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Offer workflow = catalog.getOfferById(Constants.CREATE_SUSBCRIBER.name());
		if (workflow != null) {
			String subscriberId = ((CreateSubscriberRequest)this.getRequest()).getSubscriber().getMsisdn();
			try {
				tasks.addAll(workflow.execute(subscriberId, SubscriptionLifeCycleEvent.PURCHASE, true, null));
			} catch (CatalogException e) {
				// TODO - Logger unable to fetch the workflow configured for the use-case
				throw new TransactionException(this.getRequestId(), "Unable to pack the workflow tasks for this use-case", e);
			}
			tasks.add(new Persistence<com.ericsson.raso.sef.core.db.model.Subscriber>(PersistenceMode.SAVE, subscriberEntity, subscriberEntity.getMsisdn()));
			
		}
		
		Orchestration execution = OrchestrationManager.getInstance().createExecutionProfile(this.getRequestId(), tasks);
		
		OrchestrationManager.getInstance().submit(this, execution);
		
		return true;
	}
	
	@Override
	public void sendResponse() {
		//TODO: implement this logic
		/*
		 * 1. when this method is called, it means that Orchestration Manager has executed all steps in the transaction. Either a respnse or
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

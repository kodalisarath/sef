package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.Subscriber;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.OfferManager;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.bes.prodcat.tasks.PersistenceMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;

public class CreateSubscriber extends AbstractTransaction {
	private static final long	serialVersionUID	= 8085575039162225609L;
	
	
	public CreateSubscriber(String requestId, Subscriber subscriber) {
		super(requestId, new CreateSubscriberRequest(requestId, subscriber));
	}
	
	
	@Override
	public Void execute() throws TransactionException {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>(); 
		
		
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity = ((CreateSubscriberRequest)this.getRequest()).persistableEntity();
		tasks.add(new Persistence<com.ericsson.raso.sef.core.db.model.Subscriber>(PersistenceMode.SAVE, subscriberEntity, subscriberEntity.getMsisdn()));
		
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
		}
		
		
		
		return null;
	}

}

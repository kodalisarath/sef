package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.DeleteSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.DeleteSubscriberResponse;
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
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;

public class DeleteSubscriber extends AbstractTransaction {
	private static final long	serialVersionUID	= 8085575039162225609L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSubscriber.class);
	
	public DeleteSubscriber(String requestId, Subscriber subscriber) {
		super(requestId, new CreateSubscriberRequest(requestId, subscriber));
		this.setResponse(new CreateSubscriberResponse(requestId));
	}
	
	
	@Override
	public Boolean execute() throws TransactionException {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>(); 
		
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity = ((DeleteSubscriberRequest)this.getRequest()).persistableEntity();
		tasks.add(new Persistence<com.ericsson.raso.sef.core.db.model.Subscriber>(PersistenceMode.SAVE, subscriberEntity, subscriberEntity.getMsisdn()));
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Offer workflow = catalog.getOfferById(Constants.CREATE_SUSBCRIBER.name());
		if (workflow != null) {
			String subscriberId = ((DeleteSubscriberRequest)this.getRequest()).getSubscriberId();
			try {
				tasks.addAll(workflow.execute(subscriberId, SubscriptionLifeCycleEvent.PURCHASE, true, new HashMap<String,Object>()));
			} catch (CatalogException e) {
				this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), "Unable to pack the workflow tasks for this use-case", e));
			}
		}
		Orchestration execution = OrchestrationManager.getInstance().createExecutionProfile(this.getRequestId(), tasks);
		
		OrchestrationManager.getInstance().submit(this, execution);
		
		return true;
	}
	
	public void sendResponse() {
		//TODO: implement this logic
		/*
		 * 1. when this method is called, it means that Orchestration Manager has executed all steps in the transaction. Either a respnse or
		 * exception is available.
		 * 
		 * 2. The response will most likely be results/ responses/ exceptions from atomic steps in the transaction. This must be packed into
		 * the response pojo structure pertinent to method signature of the response interface.
		 * 
		 * 3. once the response pojo entity is packed, the client for reponse interface must be invoked. the assumption is that response
		 * interface will notify the right JVM waiting for this response thru a Object.wait
		 */
		TransactionStatus txnStatus=null;
		Boolean result = ((DeleteSubscriberResponse)this.getResponse()).getResult();
		LOGGER.debug("Invoking delete subscriber response!!");
		ISubscriberResponse subscriberClient = ServiceResolver.getSubscriberResponseClient();
		subscriberClient.deleteSubscriber(this.getRequestId(), 
				                    txnStatus, 
				                    result);
		LOGGER.debug("delete susbcriber response posted");
		
	}

}

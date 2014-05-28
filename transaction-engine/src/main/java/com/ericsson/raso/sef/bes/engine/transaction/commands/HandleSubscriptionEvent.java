package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferForUserRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.FetchOfferForUserResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.HandleLifeCycleRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.HandleSubscriptionEventRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.HandleSubscriptionEventResponse;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.Orchestration;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.OrchestrationManager;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;


public class HandleSubscriptionEvent extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;

	Logger logger = LoggerFactory.getLogger(HandleSubscriptionEvent.class);
	
	public HandleSubscriptionEvent(String requestId, 
									String offerId, 
									String subscriberId, 
									String subscriptionId,
									SubscriptionLifeCycleEvent event, 
									Boolean override, 
									Map<String, Object> metas) {
		
		super(requestId, new HandleSubscriptionEventRequest(requestId, offerId, subscriberId, subscriptionId, event, override, metas));
		this.setResponse(new HandleSubscriptionEventResponse(requestId));
	}

	@Override
	public Void execute() throws TransactionException {
		logger.debug("Entered handleSubscriptionEvent!!!");
		List<TransactionTask> tasks = new ArrayList<TransactionTask>(); 
		
		IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
		Offer prodcatOffer = catalog.getOfferById(((HandleSubscriptionEventRequest)this.getRequest()).getOfferId());
		if(prodcatOffer != null)
		logger.debug("Offer retrieved from catalog: " + prodcatOffer.getName());
		try {
			String subscriptionId = ((HandleSubscriptionEventRequest)this.getRequest()).getSubscriptionId();
			if (subscriptionId != null) {
				Map<String, Object> metas = ((HandleSubscriptionEventRequest)this.getRequest()).getMetas();
				if (metas == null)
					metas = new TreeMap<String, Object>();
				
				metas.put(Constants.SUBSCRIPTION_ID.name(), subscriptionId);
				((HandleSubscriptionEventRequest)this.getRequest()).setMetas(metas);
			}
			tasks.addAll(prodcatOffer.execute(((HandleSubscriptionEventRequest)this.getRequest()).getSubscriberId(), 
												((HandleSubscriptionEventRequest)this.getRequest()).getEvent(), 
												((HandleSubscriptionEventRequest)this.getRequest()).getOverride(),
												((HandleSubscriptionEventRequest)this.getRequest()).getMetas()));
			logger.debug("Offer executed successfully!!!. Total tasks to be orchestrated = " + tasks.size());
		} catch (CatalogException e) {
			logger.error("Offer execution failed??. Cause: " + e.getMessage());
			e.printStackTrace();
			// TODO - Logger unable to fetch the workflow configured for the use-case
			throw new TransactionException(this.getRequestId(), "Unable to pack the workflow tasks for this use-case", e);
		}
		
		Orchestration execution = OrchestrationManager.getInstance().createExecutionProfile(this.getRequestId(), tasks);
		
		logger.info("Going to execute the orcheastration profile for: " + execution.getNorthBoundCorrelator());
		OrchestrationManager.getInstance().submit(this, execution);
		
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
		 * 
		 * a. If purchase event, then ensure returning subscriptionId
		 * b. For renewal, terminate, expiry, pre-renewal, pre-expiry 
		 */
	}
	
	

}

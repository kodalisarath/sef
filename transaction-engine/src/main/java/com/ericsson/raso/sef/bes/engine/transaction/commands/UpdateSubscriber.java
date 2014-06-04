package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberResponse;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.Orchestration;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.OrchestrationManager;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.bes.prodcat.tasks.PersistenceMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;
public class UpdateSubscriber extends AbstractTransaction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7686721923498952231L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSubscriber.class);
	public UpdateSubscriber(String requestId,String subscriberId,Map<String,String> metas) {
		super(requestId, new UpdateSubscriberRequest(requestId, subscriberId,metas));
		this.setResponse(new UpdateSubscriberResponse(requestId,true));
	}

	@Override
	public Boolean execute() throws TransactionException {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>(); 
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity;
		try {
			subscriberEntity = ((UpdateSubscriberRequest)this.getRequest()).persistableEntity();
			IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
			Offer workflow = catalog.getOfferById(Constants.UPDATE_SUBSCRIBER.name());
			if (workflow != null) {
				String subscriberId = ((UpdateSubscriberRequest)this.getRequest()).getSubscriberId();
				try {
					tasks.addAll(workflow.execute(subscriberId, SubscriptionLifeCycleEvent.PURCHASE, true, new HashMap<String,Object>()));
				} catch (CatalogException e) {
					this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), "Unable to pack the workflow tasks for this use-case", e));
				}
				tasks.add(new Persistence<com.ericsson.raso.sef.core.db.model.Subscriber>(PersistenceMode.SAVE, subscriberEntity, subscriberEntity.getMsisdn()));
				
			}
			
	        Orchestration execution = OrchestrationManager.getInstance().createExecutionProfile(this.getRequestId(), tasks);
			
			OrchestrationManager.getInstance().submit(this, execution);
		} catch (FrameworkException e1) {
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), "Unable to pack the workflow tasks for this use-case", e1));
		}
		
		
		return true;
	}

	@Override
	public void sendResponse() {
		
		TransactionStatus txnStatus=null;
       Boolean result = ((UpdateSubscriberResponse)this.getResponse()).getResult();
		LOGGER.debug("Invoking update subscriber response!!");
		ISubscriberResponse subscriberClient=ServiceResolver.getSubscriberResponseClient();
		subscriberClient.updateSubscriber(this.getRequestId(),txnStatus,result);
		LOGGER.debug("update susbcriber response posted");
		
	}


}

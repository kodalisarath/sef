package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.entities.HandleLifeCycleRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.HandleLifeCycleResponse;
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
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;

public class HandleLifeCycle extends AbstractTransaction{
	private static final long serialVersionUID = 1128250929086409651L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HandleLifeCycle.class);
	
	
	public HandleLifeCycle(String requestId, String subscriberId,String lifeCycleState,Map<String,String> metas) {
		super(requestId, new HandleLifeCycleRequest(requestId, subscriberId,lifeCycleState, metas));
		this.setResponse(new HandleLifeCycleResponse(requestId,true));
	}

	@Override
	public Boolean execute() throws TransactionException {
		

		List<TransactionTask> tasks = new ArrayList<TransactionTask>(); 
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity;
		try {
			subscriberEntity = ((HandleLifeCycleRequest)this.getRequest()).persistableEntity();
			
			if(subscriberEntity == null){
				this.getResponse().setReturnFault(new TransactionException("txe", new ResponseCode(504, "Subscriber not found")));
				this.sendResponse();
			}
//			this.updateChanges(subscriberEntity, 
//					((HandleLifeCycleRequest)this.getRequest()).getSubscriberId(), 
//					((HandleLifeCycleRequest)this.getRequest()).getLifeCycleState(), 
//								((HandleLifeCycleRequest)this.getRequest()).getMetas());
			tasks.add(new Persistence<com.ericsson.raso.sef.core.db.model.Subscriber>(PersistenceMode.UPDATE, subscriberEntity, subscriberEntity.getMsisdn()));
			
			// Find workflow...
			String workflowId = ((HandleLifeCycleRequest)this.getRequest()).getMetas().get(Constants.HANDLE_LIFE_CYCLE.name());
			LOGGER.debug("Workflow requested: " + workflowId);
			IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
			Offer workflow = catalog.getOfferById(workflowId);
			if (workflow != null) {
				LOGGER.debug("Workflow found in store: " + workflow);
				String subscriberId = ((HandleLifeCycleRequest)this.getRequest()).getSubscriberId();
				try {
					tasks.addAll(workflow.execute(subscriberId, SubscriptionLifeCycleEvent.PURCHASE, true, 
							this.getProdCatMap(((HandleLifeCycleRequest)this.getRequest()).getMetas())));
				} catch (CatalogException e) {
					this.getResponse().setReturnFault( new TransactionException(this.getRequestId(), "Unable to pack the workflow tasks for this use-case", e));
				}
			}
			
			Orchestration execution = OrchestrationManager.getInstance().createExecutionProfile(this.getRequestId(), tasks);
			
			OrchestrationManager.getInstance().submit(this, execution);
		} catch (FrameworkException e1) {
			this.getResponse().setReturnFault( new TransactionException(this.getRequestId(), "Unable to pack the workflow tasks for this use-case", e1));
		}
		return true;
	
		
	}

	private Subscriber updateChanges(Subscriber subscriberEntity, String subscriberId, String lifeCycleState, Map<String, String> map) {
		
		if (lifeCycleState != null && !lifeCycleState.isEmpty()) 
				subscriberEntity.setContractState(ContractState.valueOf(lifeCycleState));
		
		
		Collection<Meta> existingMetas = subscriberEntity.getMetas();
		List<Meta> toUpdate = TransactionServiceHelper.getSefCoreList(map);
		
		for (Meta newMeta: existingMetas) {
			if (map.containsKey(newMeta.getKey())) {
				existingMetas.remove(newMeta);
			}
		}
		existingMetas.addAll(toUpdate);
		
		return subscriberEntity;
	}

	@Override
	public void sendResponse() {
		
		TransactionStatus txnStatus=null;
		Boolean result = ((HandleLifeCycleResponse)this.getResponse()).getResult();
		LOGGER.debug("Invoking Handlelifecycle subscriber response!!");
		ISubscriberResponse subscriberClient = ServiceResolver.getSubscriberResponseClient();
		subscriberClient.handleLifeCycle(this.getRequestId(),
				                        txnStatus, 
				                        result);
		LOGGER.debug("Handlelifecycle susbcriber response posted");
		
	}
	
	private Map<String, Object> getProdCatMap(Map<String, String> originalMetas) {
		Map<String, Object> resultant = new HashMap<String, Object>();
		if (originalMetas != null) {
			for (String key: originalMetas.keySet()) {
				resultant.put(key, originalMetas.get(key));
			}
		}
		return resultant;
		
	}

}

package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.CreateSubscriberResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberRequest;
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
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;
import com.ericsson.sef.scheduler.command.RecycleJobCommand;

public class CreateSubscriber extends AbstractTransaction {
	private static final long	serialVersionUID	= 8085575039162225609L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateSubscriber.class);
	
	
	public CreateSubscriber(String requestId, Subscriber subscriber) {
		super(requestId, new CreateSubscriberRequest(requestId, subscriber));
		this.setResponse(new CreateSubscriberResponse(requestId));
	}
	
	
	@Override
	public Boolean execute() throws TransactionException {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>(); 
		
		
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity = null;
		try {
			LOGGER.debug("In execute method before getting persistable Entity");
			subscriberEntity = ((CreateSubscriberRequest)this.getRequest()).getEntityFromDb();
			if(subscriberEntity != null){
				this.getResponse().setReturnFault(new TransactionException("tx-engine", new ResponseCode(4020,"Invalid Operation State")));
				sendResponse();
			}else{
				subscriberEntity = ((CreateSubscriberRequest) this.getRequest()).persistableEntity();
				IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
				Offer workflow = catalog.getOfferById(Constants.CREATE_SUBSCRIBER.name());
				if (workflow != null) {
					LOGGER.debug("Workflow is not null"+workflow.getName());
					String subscriberId = ((CreateSubscriberRequest)this.getRequest()).getSubscriber().getMsisdn();
					try {
						tasks.addAll(workflow.execute(subscriberId, SubscriptionLifeCycleEvent.PURCHASE, true, new HashMap<String, Object>()));
					} catch (CatalogException e) {
						LOGGER.debug("Catch block catalog exception ",e);
						this.getResponse().setReturnFault(new TransactionException("txe", new ResponseCode(999, "Unable to pack the workflow tasks"), e));
					}
					tasks.add(new Persistence<com.ericsson.raso.sef.core.db.model.Subscriber>(PersistenceMode.SAVE, subscriberEntity, subscriberEntity.getMsisdn()));
					
				}
				
				Orchestration execution = OrchestrationManager.getInstance().createExecutionProfile(this.getRequestId(), tasks);
				
				OrchestrationManager.getInstance().submit(this, execution);
			}
			
		} catch (FrameworkException e1) {
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), new ResponseCode(11614, "Unable to pack the workflow tasks for this use-case"), e1));
			sendResponse();
		}
		sendResponse();
		return true;
	}
	
	@Override
	public void sendResponse() {
		// TODO: implement this logic
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
		
		
		LOGGER.debug("Invoking create subscriber response!!");
		boolean result = true;
		TransactionStatus txnStatus = new TransactionStatus();
		if (this.getResponse() != null) {
			if (this.getResponse() != null && this.getResponse().getReturnFault() != null) {
				TransactionException fault = this.getResponse().getReturnFault();
				if (fault != null) {
					txnStatus.setCode(fault.getStatusCode().getCode());
					txnStatus.setDescription(fault.getStatusCode().getMessage());
					txnStatus.setComponent(fault.getComponent());
					result = false;
				} else {
					//TODO: SMART specific hack on subscriber lifecycle
					String recycleDays = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "preActivePeriod");
					long recycleSchedule = 0;
					try {
						recycleSchedule = System.currentTimeMillis() + Integer.parseInt(recycleDays) * 86400000L;
					} catch(Exception e) {
						LOGGER.error("Recycle Period was not/badly configured. Assuming 1 year!!");
						recycleSchedule = System.currentTimeMillis() + 31536000000L;
					}
					try {
						new RecycleJobCommand(((CreateSubscriberRequest)this.getRequest()).getSubscriber(), new Date(recycleSchedule)).execute();
					} catch (SmException e) {
						txnStatus.setCode(e.getStatusCode().getCode());
						txnStatus.setDescription(e.getStatusCode().getMessage());
						txnStatus.setComponent(e.getComponent());
					}
				}
			}
		} else
			result = false;

		

		LOGGER.debug("CreateSubscriber::=> Functional Result: " + result);
		
		
		LOGGER.debug("About to send request...");
		ISubscriberResponse subscriberClient = ServiceResolver.getSubscriberResponseClient();
		if (subscriberClient != null) {
			LOGGER.debug("see what is null here? - requestId: " + this.getRequestId() + ", txnStatus: " + txnStatus + ", result: " + result);
			subscriberClient.createSubscriber(this.getRequestId(), 
					txnStatus, 
					result);
			LOGGER.debug("create susbcriber response posted");
		} else {
			LOGGER.error("Unable to acquire client access to response interface. Request will time-out in the consumer side!!");
		}

		
	}

}

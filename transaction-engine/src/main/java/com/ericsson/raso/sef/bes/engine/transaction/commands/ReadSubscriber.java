package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.entities.ReadSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.ReadSubscriberResponse;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.FulfillmentStep;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.Orchestration;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.OrchestrationManager;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.Step;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.tasks.FetchSubscriber;
import com.ericsson.raso.sef.bes.prodcat.tasks.TaskType;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;


public class ReadSubscriber extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadSubscriber.class);
	
	private boolean isWorkflowEngaged = false;

	public ReadSubscriber(String requestId, String subscriberId, Map<String, String> metas) {
		super(requestId, new ReadSubscriberRequest(requestId, subscriberId, metas));
		this.setResponse(new ReadSubscriberResponse(requestId));
	}

	@Override
	public Boolean execute() throws TransactionException {
		LOGGER.debug("Entering Read Subscriber somehow....");
		
		try {
			
			//Fetch subscriber from DB
			Subscriber subscriber = new FetchSubscriber(((ReadSubscriberRequest)this.getRequest()).getSubscriberId()).execute();
			LOGGER.debug("Subscriber entity fetched from DB: " + subscriber);
			
			
			//Subscriber not found in db, cannot proceed further
			if(subscriber == null) {
				throw new TransactionException("txe", new ResponseCode(102, "Subscriber not found"));
			}

			com.ericsson.sef.bes.api.entities.Subscriber result = TransactionServiceHelper.getApiEntity(subscriber);
			((ReadSubscriberResponse)this.getResponse()).setSubscriber(result);


			// Fetch the subscriber from downstream through work flow
			String readSubscriberWorkflowId = ((ReadSubscriberRequest)this.getRequest()).getMetas().get(Constants.READ_SUBSCRIBER.name());
			if(readSubscriberWorkflowId != null) {
				LOGGER.debug("somehow a workflow is also needed... wtf?!!!");
				IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
				Offer workflow = catalog.getOfferById(readSubscriberWorkflowId);
				
				LOGGER.debug("Guess what? found the workflow in prodcat too...");
				if (workflow != null) {
					List<TransactionTask> tasks = new ArrayList<TransactionTask>(); 
					tasks.addAll(workflow.execute(((ReadSubscriberRequest)this.getRequest()).getSubscriberId(), 
												SubscriptionLifeCycleEvent.DISCOVERY, 
												true, 
												TransactionServiceHelper.getNativeMap(((ReadSubscriberRequest)this.getRequest()).getMetas())));

					this.isWorkflowEngaged = true;
					Orchestration execution = OrchestrationManager.getInstance().createExecutionProfile(this.getRequestId(), tasks);
					LOGGER.info("Going to execute the orcheastration profile for: " + execution.getNorthBoundCorrelator());
					OrchestrationManager.getInstance().submit(this, execution);
				}
			} 
		} catch (TransactionException e) {
			((ReadSubscriberResponse)this.getResponse()).setReturnFault(e);
		} catch (FrameworkException e1) {
			((ReadSubscriberResponse)this.getResponse()).setReturnFault(new TransactionException(e1.getComponent(), e1.getStatusCode()));
		} finally {
			
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
		
		TransactionStatus txnStatus=null;
		com.ericsson.sef.bes.api.entities.Subscriber subscriber = ((ReadSubscriberResponse)this.getResponse()).getSubscriber();
			
		if (this.isWorkflowEngaged) {
			List<Product> products = new ArrayList<Product>();
			
			for (Step<?> result: this.getResponse().getAtomicStepResults().keySet()) {
				if (result.getExecutionInputs().getType() == TaskType.FULFILLMENT) {
					if(result.getFault() != null){
						txnStatus=new TransactionStatus();
						txnStatus.setCode(result.getFault().getStatusCode().getCode());
						txnStatus.setComponent(result.getFault().getComponent());
						txnStatus.setDescription(result.getFault().getStatusCode().getMessage());
						break;
					}else{
						if(((FulfillmentStep) result).getResult() !=null)
						products.addAll(TransactionServiceHelper.translateProducts(((FulfillmentStep) result).getResult().getFulfillmentResult()));
					}
				}
			}
			
			subscriber = TransactionServiceHelper.enrichSubscriber(subscriber, products);
		}
		
		LOGGER.debug("Invoking read subscriber response!!");
		ISubscriberResponse subscriberClient = ServiceResolver.getSubscriberResponseClient();
		subscriberClient.readSubscriber(this.getRequestId(), 
				                    txnStatus, 
									subscriber);
		LOGGER.debug("read susbcriber response posted");

		
	}
	
	

}

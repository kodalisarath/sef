package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;

public class PersistenceStep extends Step<PersistenceStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;
	private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceStep.class);

	PersistenceStep(String stepCorrelator, Persistence<?> executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public PersistenceStepResult execute() {
		Object returned = null;
		StepExecutionException fault = null;
		
		Object persistentEntity = ((Persistence) this.getExecutionInputs()).getToSave();


		try {
			LOGGER.debug("Quick Type of Persistence Entity: " + persistentEntity.getClass().getCanonicalName());
			if (persistentEntity instanceof Subscriber) {
				LOGGER.debug("Entity is indeed a Subscriber...");
				SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
				LOGGER.debug("Do I have the bean ready? " + subscriberService);
				switch (((Persistence) this.getExecutionInputs()).getMode()) {
					case QUERY:
						LOGGER.debug("About to query Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						try {
							returned = subscriberService.getSubscriber(this.getStepCorrelator(), ((Subscriber) persistentEntity).getMsisdn());
						} catch (PersistenceError e) {
							fault = new StepExecutionException("txe", new ResponseCode(7554, "Internal error: A database access exception occurred"), e);
						}
						LOGGER.debug("Fetched Entity for query.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(fault, returned));
						} else {
							this.getResult().setPersistenceResult(returned);
							this.getResult().setResultantFault(fault);
						}
						
						break;
					case REMOVE:
						LOGGER.debug("PERSISTENT ENTITY REMOVE: " + persistentEntity);
						try {
							returned = subscriberService.fulldeleteSubscriber(this.getStepCorrelator(), ((Subscriber) persistentEntity));
						} catch (PersistenceError e) {
							fault = new StepExecutionException("txe", new ResponseCode(7554, "Internal error: A database access exception occurred"), e);
						}
						LOGGER.debug("Subscriber in dunning state.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(fault, returned));
						} else {
							this.getResult().setPersistenceResult(true);
							this.getResult().setResultantFault(fault);
						}
						
						break;
					case UPDATE:
						LOGGER.debug("PERSISTENT ENTITY UPDATE: " + persistentEntity);
						try {
							returned  = subscriberService.updateSubscriber(this.getStepCorrelator(), ((Subscriber) persistentEntity));
						} catch (PersistenceError e) {
							fault = new StepExecutionException("txe", new ResponseCode(7554, "Internal error: A database access exception occurred"), e);
						}
						LOGGER.debug("Subscriber updated.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(fault, returned));
						} else {
							this.getResult().setPersistenceResult(true);
							this.getResult().setResultantFault(fault);
						}
						
						break;
					case SAVE:
						LOGGER.debug("PERSISTENT ENTITY SAVE: " + persistentEntity);
						try {
							returned = subscriberService.createSubscriber(this.getStepCorrelator(), (Subscriber) persistentEntity); 
						} catch (PersistenceError e) {
							fault = new StepExecutionException("txe", new ResponseCode(7554, "Internal error: A database access exception occurred"), e);
						}
						LOGGER.debug("Subscriber saved.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(fault, returned));
						} else
							this.getResult().setPersistenceResult(fault);
						
						break;
					default:
						LOGGER.debug("Why is my mode in default? " + ((Persistence) this.getExecutionInputs()).getMode());
				}
			}
			
			if (persistentEntity instanceof Subscription) {
				LOGGER.debug("Indeed a subscription entity...");
				//SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
				switch (((Persistence) this.getExecutionInputs()).getMode()) {
					case QUERY:
						LOGGER.debug("About to query Subscription: " + ((Subscription) persistentEntity).getSubscriberId());
						returned = (Subscription) persistentEntity; 											//TODO: implement...
						LOGGER.debug("Fetched Entity for query.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, returned));
						} else
							this.getResult().setPersistenceResult(returned);
						
						break;
					case REMOVE:
						LOGGER.debug("Deleting Subscription: " + ((Subscription) persistentEntity).getSubscriberId());
						returned = true;																		//TODO: implement...
						LOGGER.debug("Subscriber deleted.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, true));
						} else
							this.getResult().setPersistenceResult(true);
						
						break;
					case SAVE:
						LOGGER.debug("Saving Subscription: " + ((Subscription) persistentEntity).getSubscriberId());
						returned = true;																		//TODO: implement...
						LOGGER.debug("Subscriber saved.");
						
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, true));
						} else
							this.getResult().setPersistenceResult(true);

						break;
					default:
						LOGGER.debug("Why is my mode here? " + ((Persistence) this.getExecutionInputs()).getMode());
				}
			}
			return this.getResult();
						
		} catch(Exception e) {			
			LOGGER.error("Failed Persistence task", e);
			
			if (this.getResult() == null) {
				this.setResult(new PersistenceStepResult(new StepExecutionException("txe", new ResponseCode(6000, "Persistence Tier Failed"), e), null));
			} else {
				this.getResult().setResultantFault(new StepExecutionException("txe", new ResponseCode(6000, "Persistence Tier Failed"), e));	
			}
			return this.getResult();
		}
	}

	@Override
	public String toString() {
		return "<PersistenceStep executionInputs='" + getExecutionInputs() + "' getResult='" + getResult() + "/>";
	}


}

package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.Subscriber;
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
		Object persistentEntity = ((Persistence) this.getExecutionInputs()).getToSave();


		try {
			if (persistentEntity instanceof Subscriber) {
				SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
				switch (((Persistence) this.getExecutionInputs()).getMode()) {
					case QUERY:
						LOGGER.debug("About to query Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						returned = subscriberService.getSubscriber(this.getStepCorrelator(), ((Subscriber) persistentEntity).getMsisdn());
						LOGGER.debug("Fetched Entity for query.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, returned));
						} else
							this.getResult().setPersistenceResult(returned);
						
						break;
					case REMOVE:
						LOGGER.debug("Deleting Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						subscriberService.fulldeleteSubscriber(this.getStepCorrelator(), ((Subscriber) persistentEntity));
						LOGGER.debug("Subscriber deleted.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, true));
						} else
							this.getResult().setPersistenceResult(true);
						
						break;
					case UPDATE:
						LOGGER.debug("Updating Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						subscriberService.updateSubscriber(this.getStepCorrelator(), ((Subscriber) persistentEntity));
						LOGGER.debug("Subscriber updated.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, true));
						} else
							this.getResult().setPersistenceResult(true);
						
						break;
					case SAVE:
						LOGGER.debug("Saving Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						subscriberService.createSubscriber(this.getStepCorrelator(), (Subscriber) persistentEntity); 
						/*if (subscriberService.isSubscriberExist(((Subscriber) persistentEntity).getMsisdn())) 
							throw 
							subscriberService.updateSubscriber(this.getStepCorrelator(), (Subscriber) persistentEntity); 
						else*/
						LOGGER.debug("Subscriber saved.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, true));
						} else
							this.getResult().setPersistenceResult(true);
						
						break;
					default:

				}
			}
			
			if (persistentEntity instanceof Subscription) {
				//SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
				switch (((Persistence) this.getExecutionInputs()).getMode()) {
					case QUERY:
						LOGGER.debug("About to query Subscriber: " + ((Subscription) persistentEntity).getSubscriberId());
						returned = (Subscription) persistentEntity; 											//TODO: implement...
						LOGGER.debug("Fetched Entity for query.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, returned));
						} else
							this.getResult().setPersistenceResult(returned);
						
						break;
					case REMOVE:
						LOGGER.debug("Deleting Subscriber: " + ((Subscription) persistentEntity).getSubscriberId());
						returned = true;																		//TODO: implement...
						LOGGER.debug("Subscriber deleted.");
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, true));
						} else
							this.getResult().setPersistenceResult(true);
						
						break;
					case SAVE:
						LOGGER.debug("Saving Subscriber: " + ((Subscription) persistentEntity).getSubscriberId());
						returned = true;																		//TODO: implement...
						LOGGER.debug("Subscriber saved.");
						
						
						if (this.getResult() == null) {
							this.setResult(new PersistenceStepResult(null, true));
						} else
							this.getResult().setPersistenceResult(true);

						break;
					default:

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

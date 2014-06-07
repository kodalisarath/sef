package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
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
						returned = subscriberService.getSubscriber(((Subscriber) persistentEntity).getMsisdn());
						LOGGER.debug("Fetched Entity for query.");
						this.getResult().setPersistenceResult(returned);
						break;
					case REMOVE:
						LOGGER.debug("Deleting Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						subscriberService.deleteSubscriber(((Subscriber) persistentEntity).getMsisdn());
						LOGGER.debug("Subscriber deleted.");
						this.getResult().setPersistenceResult(true);
						break;
					case SAVE:
						LOGGER.debug("Saving Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						subscriberService.createSubscriber((Subscriber) persistentEntity);
						LOGGER.debug("Subscriber saved.");
						this.getResult().setPersistenceResult(true);
						break;
					default:

				}
			}
			
			if (persistentEntity instanceof Subscription) {
				//SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
				switch (((Persistence) this.getExecutionInputs()).getMode()) {
					case QUERY:
						LOGGER.debug("About to query Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						returned = (Subscription) persistentEntity; 											//TODO: implement...
						LOGGER.debug("Fetched Entity for query.");
						this.getResult().setPersistenceResult(returned);
						break;
					case REMOVE:
						LOGGER.debug("Deleting Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						returned = true;																		//TODO: implement...
						LOGGER.debug("Subscriber deleted.");
						this.getResult().setPersistenceResult(true);
						break;
					case SAVE:
						LOGGER.debug("Saving Subscriber: " + ((Subscriber) persistentEntity).getMsisdn());
						returned = true;
						LOGGER.debug("Subscriber saved.");
						this.getResult().setPersistenceResult(true);
						break;
					default:

				}
			}
			
						
		} catch(Exception e) {			
			LOGGER.error("Failed Persistence task", e);
			this.getResult().setResultantFault(new StepExecutionException("txe", new ResponseCode(6000, "Persistence Tier Failed"), e));
			return this.getResult();
		}
		
		return this.getResult();
	}

	@Override
	public String toString() {
		return "<PersistenceStep executionInputs='" + getExecutionInputs() + "' getResult='" + getResult() + "/>";
	}


}

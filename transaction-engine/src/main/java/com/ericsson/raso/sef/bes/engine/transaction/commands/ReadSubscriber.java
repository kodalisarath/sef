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
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.AbstractStepResult;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.FulfillmentStepResult;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.Orchestration;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.OrchestrationManager;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.PersistenceStep;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.Step;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.tasks.TaskType;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;

public class ReadSubscriber extends AbstractTransaction {

	private static final long serialVersionUID = 8130277491237379246L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReadSubscriber.class);

	private boolean isWorkflowEngaged = false;

	public ReadSubscriber(String requestId, String subscriberId,
			Map<String, String> metas) {
		super(requestId, new ReadSubscriberRequest(requestId, subscriberId,
				metas));
		this.setResponse(new ReadSubscriberResponse(requestId));
	}

	@Override
	public Boolean execute() throws TransactionException {
		LOGGER.debug("Entering Read Subscriber somehow....");

		try {

			// Fetch subscriber from DB
			LOGGER.debug("SANITY CHECKS....");
			LOGGER.debug("Request: " + this.getRequest());

			Subscriber subscriber = TransactionServiceHelper
					.fetchSubscriberFromDb(((ReadSubscriberRequest) this
							.getRequest()).getSubscriberId());
			LOGGER.debug("Subscriber entity fetched from DB: " + subscriber);

			// Subscriber not found in db, cannot proceed further
			if (subscriber == null) {
				throw new TransactionException("txe", new ResponseCode(504,
						"Subscriber not found"));
			}

			com.ericsson.sef.bes.api.entities.Subscriber result = TransactionServiceHelper
					.getApiEntity(subscriber);
			((ReadSubscriberResponse) this.getResponse()).setSubscriber(result);

			// Fetch the subscriber from downstream through work flow
			String readSubscriberWorkflowId = ((ReadSubscriberRequest) this
					.getRequest()).getMetas().get(
					Constants.READ_SUBSCRIBER.name());
			if (readSubscriberWorkflowId != null) {
				LOGGER.debug("somehow a workflow is also needed... wtf?!!!");
				IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
				Offer workflow = catalog.getOfferById(readSubscriberWorkflowId);

				LOGGER.debug("Guess what? found the workflow in prodcat too...");
				if (workflow != null) {
					List<TransactionTask> tasks = new ArrayList<TransactionTask>();
					tasks.addAll(workflow.execute(((ReadSubscriberRequest) this
							.getRequest()).getSubscriberId(),
							SubscriptionLifeCycleEvent.DISCOVERY, true,
							TransactionServiceHelper
									.getNativeMap(((ReadSubscriberRequest) this
											.getRequest()).getMetas())));

					this.isWorkflowEngaged = true;
					Orchestration execution = OrchestrationManager
							.getInstance().createExecutionProfile(
									this.getRequestId(), tasks);
					LOGGER.info("Going to execute the orcheastration profile for: "
							+ execution.getNorthBoundCorrelator());
					OrchestrationManager.getInstance().submit(this, execution);
				}
			}
		} catch (TransactionException e) {
			LOGGER.error(
					"ReadSubscriber TransactionException caught "
							+ e.getMessage(), e);
/*			((ReadSubscriberResponse) this.getResponse()).setReturnFault(e);
			sendResponse();
*/			
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), new ResponseCode(11614, "Unable to pack the workflow tasks for this use-case"), e));
			
			return false;
		} catch (FrameworkException e1) {
			LOGGER.error(
					"ReadSubscriber FrameworkException caught "
							+ e1.getMessage(), e1);
			/*((ReadSubscriberResponse) this.getResponse())
					.setReturnFault(new TransactionException(e1.getComponent(),
							e1.getStatusCode()));
*/
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), new ResponseCode(11615, "Unable to pack the workflow tasks for this use-case"), e1));
			sendResponse();
			return false;
		} finally {

		}

		return true;
	}

	@Override
	public void sendResponse() {
		// TODO: implement this logic
		/*
		 * 1. when this method is called, it means that Orchestration Manager
		 * has executed all steps in the transaction. Either a response or
		 * exception is available.
		 * 
		 * 2. The response will most likely be results/ responses/ exceptions
		 * from atomic steps in the transaction. This must be packed into the
		 * response pojo structure pertinent to method signature of the response
		 * interface.
		 * 
		 * 3. once the response pojo entity is packed, the client for response
		 * interface must be invoked. the assumption is that response interface
		 * will notify the right JVM waiting for this response thru a
		 * Object.wait
		 */

		TransactionStatus txnStatus = new TransactionStatus();
		com.ericsson.sef.bes.api.entities.Subscriber subscriber = ((ReadSubscriberResponse) this
				.getResponse()).getSubscriber();

		if (this.isWorkflowEngaged) {
			List<Product> products = new ArrayList<Product>();

			for (Step<?> step : this.getResponse().getAtomicStepResults()
					.keySet()) {
				if (step.getExecutionInputs().getType() == TaskType.FULFILLMENT) {
					if (step.getFault() != null) {
						txnStatus.setCode(step.getFault().getStatusCode()
								.getCode());
						txnStatus.setComponent(step.getFault().getComponent());
						txnStatus.setDescription(step.getFault()
								.getStatusCode().getMessage());
						break;
					} else {
						// products.addAll(TransactionServiceHelper.translateProducts(((FulfillmentStep)
						// step).getResult().getFulfillmentResult()));
						FulfillmentStepResult stepResult = (FulfillmentStepResult) this
								.getResponse().getAtomicStepResults().get(step);
						if (stepResult != null) {
							for (AtomicProduct atomicProduct : stepResult
									.getFulfillmentResult()) {
								LOGGER.debug("Atomic product metas: "
										+ atomicProduct.getMetas().toString());
							}
							products.addAll(TransactionServiceHelper
									.translateProducts(stepResult
											.getFulfillmentResult()));
							// TODO: go back and refactor all the way from
							// FulfillmentStep to pass metas to SMFE....
							LOGGER.debug("FulfillmentStep has some results added to products list");
						}
					}
				}
			}

			subscriber = TransactionServiceHelper.enrichSubscriber(subscriber,
					products);

		}

		else if (this.getResponse() != null) {
			TransactionException fault = this.getResponse().getReturnFault();
			if (fault != null) {
				txnStatus.setCode(fault.getStatusCode().getCode());
				txnStatus.setDescription(fault.getStatusCode().getMessage());
				txnStatus.setComponent(fault.getComponent());
			}

		}

		LOGGER.debug("Invoking read subscriber response!!");
		ISubscriberResponse subscriberClient = ServiceResolver
				.getSubscriberResponseClient();
		LOGGER.debug("subscriberClient" + subscriberClient);
		subscriberClient.readSubscriber(this.getRequestId(), txnStatus,
				subscriber);
		LOGGER.debug("read susbcriber response posted");

	}

}

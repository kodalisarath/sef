package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.fulfillment.FulfillmentRequest;

public class FulfillmentStep extends Step<FulfillmentStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;
	private static final Logger logger = LoggerFactory.getLogger(FulfillmentStep.class);

	FulfillmentStep(String stepCorrelator, Fulfillment executionInputs) {
		super(stepCorrelator, executionInputs);
	}


	
	@Override
	public FulfillmentStepResult execute() {
		
		try{
		logger.debug("Preparing for fulfillment");
		AtomicProduct atomicProduct = ((Fulfillment)this.getExecutionInputs()).getAtomicProduct();
		String subscriberId = ((Fulfillment)this.getExecutionInputs()).getSubscriberId();
		Map<String, Object> additionalInputs = ((Fulfillment)this.getExecutionInputs()).getAdditionalInputs();
		
		//------------------------------------------------------------------------------------
		// Remove these loggers post FT
		//====================================================================================
		
		logger.debug("retrieved fulfillment inputs, product: " +  atomicProduct.getName() + "subscriber: " +  subscriberId);
		logger.debug("Resource name: " + atomicProduct.getResource().getName());
//		logger.debug("Validity: " + atomicProduct.getValidity().getExpiryTimeInMillis());
//		logger.debug("Quota: " + atomicProduct.getQuota().getDefinedQuota());
		
		
		
		com.ericsson.sef.bes.api.entities.Product product = new com.ericsson.sef.bes.api.entities.Product();
		product.setName(atomicProduct.getName());
		product.setResourceName(atomicProduct.getResource().getName());
		//product.setValidity(atomicProduct.getValidity().getExpiryTimeInMillis());
		//product.setQuotaDefined(atomicProduct.getQuota().getDefinedQuota());
		List<Meta> metas = converToList(additionalInputs);
		//TODO: Impedence on the interface to accept object. refactoring required in product catalog
		
		logger.debug("Major milestone.. going to fulfill the service provisioning");
		FulfillmentRequest request = ServiceResolver.getFulfillmentRequestClient();
		request.fulfill(getStepCorrelator(), subscriberId, product, metas);
		logger.info("Fulfillment completed!!!.. going to update results");
		
		return new FulfillmentStepResult(null, null);
		
		} catch(Exception e) {
			logger.debug("Exception in execution of fulfillment: Exception: " +  e);
			FulfillmentStepResult result = new FulfillmentStepResult(new StepExecutionException("FulfilmentExecution failed"), null);
			Map<String, AbstractStepResult> stepResultStore = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.TRANSACTION_STEP_STATUS.name());
			stepResultStore.put(stepCorrelator, result);
			Map<String, Orchestration> orchestrationStore = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.ORCHESTRATION_TASK_MAPPER.name());
			orchestrationStore.get(stepResultStore);
			OrchestrationManager.getInstance().promoteFulfillmentExecution(stepCorrelator, result); 
			return result;
		}
	}

	private List<Meta> converToList(Map<String, Object> metas) {
		List<Meta> metaList = new ArrayList<Meta>();
		if(metas != null) {
		for (String metaskey: metas.keySet()) {
			Meta meta = new Meta();
			if(metas.get(metaskey) instanceof String){
				meta.setKey(metaskey);
				meta.setValue(metas.get(metaskey).toString());
				metaList.add(meta);
			}
		}
		}
		return metaList;
	}
	

}

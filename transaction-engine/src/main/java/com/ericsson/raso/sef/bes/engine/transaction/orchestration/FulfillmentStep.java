package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.fulfillment.FulfillmentRequest;

public class FulfillmentStep extends Step<FulfillmentStepResult> {
	private static final long	serialVersionUID	= 6645187522590773212L;

	FulfillmentStep(String stepCorrelator, Fulfillment executionInputs) {
		super(stepCorrelator, executionInputs);
	}


	
	@Override
	public FulfillmentStepResult execute() {
		AtomicProduct atomicProduct = ((Fulfillment)this.getExecutionInputs()).getAtomicProduct();
		String subscriberId = ((Fulfillment)this.getExecutionInputs()).getSubscriberId();
		Map<String, Object> additionalInputs = ((Fulfillment)this.getExecutionInputs()).getAdditionalInputs();
		
		com.ericsson.sef.bes.api.entities.Product product = new com.ericsson.sef.bes.api.entities.Product();
		product.setName(atomicProduct.getName());
		product.setResourceName(atomicProduct.getResource().getName());
		product.setValidity(atomicProduct.getValidity().getExpiryTimeInMillis());
		product.setQuotaDefined(atomicProduct.getQuota().getDefinedQuota());
		
		//TODO: Impedence on the interface to accept object. refactoring required in product catalog
		List<Meta> metas = converToList(additionalInputs);
		
		FulfillmentRequest request = ServiceResolver.getFulfillmentRequestClient();
		request.fulfill(getStepCorrelator(), subscriberId, product, metas);
		
		
		Set<AtomicProduct> result = new TreeSet<AtomicProduct>();
		result.add(((Fulfillment)this.getExecutionInputs()).getAtomicProduct());
		return new FulfillmentStepResult(null, result);
	}

	private List<Meta> converToList(Map<String, Object> metas) {
		List<Meta> metaList = new ArrayList<Meta>();
		for (String metaskey: metas.keySet()) {
			Meta meta = new Meta();
			if(metas.get(metaskey) instanceof String){
				meta.setKey(metaskey);
				meta.setValue(metas.get(metaskey).toString());
				metaList.add(meta);
			}
		}
		return metaList;
	}
	

}

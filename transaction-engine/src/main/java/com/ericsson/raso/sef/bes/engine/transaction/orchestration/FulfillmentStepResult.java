package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.util.Map;
import java.util.Set;

import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;


public class FulfillmentStepResult extends AbstractStepResult {
	private static final long	serialVersionUID	= -2540018090411449909L;

	private Set<AtomicProduct> fulfillmentResult = null;
	private Map<String, String> metas = null;
	
	
	public FulfillmentStepResult(StepExecutionException resultantFault, Set<AtomicProduct> result) {
		super(resultantFault);
		this.fulfillmentResult = result;
	}


	public Set<AtomicProduct> getFulfillmentResult() {
		return fulfillmentResult;
	}


	public void setFulfillmentResult(Set<AtomicProduct> fulfillmentResult) {
		this.fulfillmentResult = fulfillmentResult;
	}
	
	


	public Map<String, String> getMetas() {
		return metas;
	}


	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}


	@Override
	public boolean validateResult() {
		// TODO implement when service logic is needed; for now true is good enough
		if (this.fulfillmentResult == null) 
			return false;
		else
			return true;
	}


	@Override
	public String toString() {
		return "FulfillmentStepResult [" + super.toString() + "fulfillmentResult=" + fulfillmentResult + ", metas=" + metas + "]";
	}

	

		
	
}

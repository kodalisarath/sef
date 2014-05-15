package com.ericsson.raso.sef.bes.engine.transaction.orchestration;


public class FulfillmentStepResult extends AbstractStepResult {
	private static final long	serialVersionUID	= -2540018090411449909L;

	private Object fulfillmentResult = null;
	
	
	FulfillmentStepResult(StepExecutionException resultantFault, Object result) {
		super(resultantFault);
		this.fulfillmentResult = result;
	}


	public Object getFulfillmentResult() {
		return fulfillmentResult;
	}


	public void setFulfillmentResult(Object fulfillmentResult) {
		this.fulfillmentResult = fulfillmentResult;
	}

	

		
	
}

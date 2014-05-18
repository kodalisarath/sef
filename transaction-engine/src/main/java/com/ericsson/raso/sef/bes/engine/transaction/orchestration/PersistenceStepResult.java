package com.ericsson.raso.sef.bes.engine.transaction.orchestration;



public class PersistenceStepResult extends AbstractStepResult {
	private static final long	serialVersionUID	= -2540018090411449909L;

	private Object persistenceResult = null;
	
	
	PersistenceStepResult(StepExecutionException resultantFault, Object result) {
		super(resultantFault);
		this.persistenceResult = result;
	}


	public Object getPersistenceResult() {
		return persistenceResult;
	}


	public void setPersistenceResult(Object persistenceResult) {
		this.persistenceResult = persistenceResult;
	}

	
}

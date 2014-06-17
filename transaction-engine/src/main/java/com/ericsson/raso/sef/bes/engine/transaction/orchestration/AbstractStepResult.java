package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.io.Serializable;

public abstract class AbstractStepResult implements Serializable {
	private static final long	serialVersionUID	= -2540018090411449909L;

	private StepExecutionException resultantFault = null;
	
	
	AbstractStepResult(StepExecutionException resultantFault) {
		super();
		this.resultantFault = resultantFault;
	}


	public StepExecutionException getResultantFault() {
		return resultantFault;
	}


	public void setResultantFault(StepExecutionException resultantFault) {
		this.resultantFault = resultantFault;
	}


	public abstract boolean validateResult();


	@Override
	public String toString() {
		return "AbstractStepResult [resultantFault=" + resultantFault + "]";
	}
	
	
}

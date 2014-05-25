package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.io.Serializable;

public class AbstractStepResult implements Serializable {
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


	
	
}
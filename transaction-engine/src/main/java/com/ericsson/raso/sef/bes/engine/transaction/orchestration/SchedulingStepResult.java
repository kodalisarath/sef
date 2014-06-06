package com.ericsson.raso.sef.bes.engine.transaction.orchestration;


public class SchedulingStepResult extends AbstractStepResult {
	private static final long	serialVersionUID	= -2540018090411449909L;

	private Boolean schedulingResult = null;
	
	
	SchedulingStepResult(StepExecutionException resultantFault, Boolean result) {
		super(resultantFault);
		this.schedulingResult = result;
	}


	public Boolean getNotificationResult() {
		return schedulingResult;
	}


	public void setNotificationResult(Boolean scheduleResult) {
		this.schedulingResult = scheduleResult;
	}

	@Override
	public boolean validateResult() {
		// TODO implement when service logic is needed; for now true is good enough
		if (this.schedulingResult == null) 
			return false;
		else
			return true;
	}

	
}

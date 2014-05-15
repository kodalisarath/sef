package com.ericsson.raso.sef.bes.engine.transaction.orchestration;


public class NotificationStepResult extends AbstractStepResult {
	private static final long	serialVersionUID	= -2540018090411449909L;

	private Boolean notificationResult = null;
	
	
	NotificationStepResult(StepExecutionException resultantFault, Boolean result) {
		super(resultantFault);
		this.notificationResult = result;
	}


	public Boolean getNotificationResult() {
		return notificationResult;
	}


	public void setNotificationResult(Boolean notificationResult) {
		this.notificationResult = notificationResult;
	}

	
}

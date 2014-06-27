package com.ericsson.raso.sef.notification.workflows;

import org.apache.camel.Header;

import com.ericsson.raso.sef.notification.workflows.task.ActivateSubscriberTask;

public class ActivateSubscriber {
	
	public void process(@Header("msisdn") String msisdn) throws Exception {
		new ActivateSubscriberTask(msisdn).execute();
	}
}

package com.ericsson.raso.sef.notification.workflows.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.RunPeriodicAccountManagementCommand;
import com.ericsson.raso.sef.client.air.request.RunPeriodicAccountManagementRequest;
import com.ericsson.raso.sef.client.air.response.RunPeriodicAccountManagementResponse;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Task;

public class RunPeriodicAccountTask implements Task<Void> {
	
	protected static Logger log = LoggerFactory.getLogger(RunPeriodicAccountTask.class);
	
	private String msisdn;
	private Integer pamIndicator;
	
	public RunPeriodicAccountTask(String msisdn, Integer pamIndicator) {
		this.msisdn = msisdn;
		this.pamIndicator = pamIndicator;
	}

	public Void execute() throws FrameworkException {
		RunPeriodicAccountManagementRequest request = new RunPeriodicAccountManagementRequest();
		request.setSubscriberNumber(msisdn);		
		request.setPamIndicator(pamIndicator);
		request.setPamServiceID(1);
		try{
			RunPeriodicAccountManagementResponse response = new RunPeriodicAccountManagementCommand(request).execute();
			log.info("Periodic account executed."+ response);
		}catch(SmException smex){
			throw new FrameworkException(smex);
		}
		return null;
	}
}

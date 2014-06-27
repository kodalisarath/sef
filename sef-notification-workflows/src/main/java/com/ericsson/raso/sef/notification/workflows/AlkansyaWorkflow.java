package com.ericsson.raso.sef.notification.workflows;

import java.util.List;

import org.apache.camel.Header;

import com.ericsson.raso.sef.notification.workflows.task.RunPeriodicAccountTask;
import com.ericsson.raso.sef.core.Meta;

public class AlkansyaWorkflow {
	
	public void process(
			@Header("msisdn") String msisdn, 
			@Header("eventId") String eventId, 
			@Header("metas") List<Meta> metas) throws Exception {
		
		Integer pamIndicator = null;
		for (Meta meta : metas) {
			if(meta.getKey().equals("PID")) {
				pamIndicator = Integer.valueOf(meta.getValue());
			}
		}
		
		if(pamIndicator == null) {
			throw new RuntimeException("Pam indicator can not be null to execute the workflow.");
		}
		
		RunPeriodicAccountTask runPeriodicAccountTask = new RunPeriodicAccountTask(msisdn, pamIndicator);
		runPeriodicAccountTask.execute();
	}
}

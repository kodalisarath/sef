package com.ericsson.raso.sef.notification.workflows.callingcircle;

import java.util.List;

import org.apache.camel.Header;

import com.ericsson.raso.sef.core.Meta;

public class CallingCircleWorkflow {

	public void process(
			@Header("msisdn") String msisdn, 
			@Header("eventId") String eventId, 
			@Header("metas") List<Meta> metas) throws Exception {

		boolean isRemove = false;
		if(metas!= null) {
			for (Meta meta : metas) {
				if(meta.getKey().equalsIgnoreCase("action") && meta.getValue().equalsIgnoreCase("remove")) {
					isRemove = true;
					break;
				}
			}
		}
		
		if(isRemove) {
			new RemoveCallingCircleTask(metas).execute();
		} else {
			new AddCallingCircleTask(metas).execute();
		}
	}
}

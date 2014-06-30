package com.ericsson.raso.sef.notification.workflows.callingcircle;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.notification.workflows.NotificationContext;
import com.ericsson.raso.sef.notification.workflows.task.ActivateSubscriberTask;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.Task;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.sef.scheduler.command.ScheduleRemoveCallingCircleCmd;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;

public class RemoveCallingCircleTask implements Task<Void> {

	private List<Meta> metas;
	private static Logger log = LoggerFactory.getLogger(ActivateSubscriberTask.class);
	public RemoveCallingCircleTask(List<Meta> metas) {
		this.metas = metas;
	}

	@Override
	public Void execute() throws SmException {
		
		String callingCircleId = "";
		for (Meta meta : metas) {
			if(meta.getKey().equalsIgnoreCase(ScheduleRemoveCallingCircleCmd.CALLING_CIRCLE_ID)) {
				callingCircleId = meta.getValue();
			}
		}
		List<com.ericsson.sef.bes.api.entities.Meta> newMetaList = new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
		log.debug(String.format("RemoveCallingCircleTask removing callingCircleId %s", callingCircleId));
		for (Meta meta : metas) {
			com.ericsson.sef.bes.api.entities.Meta newMeta = new com.ericsson.sef.bes.api.entities.Meta();
			newMeta.setKey(meta.getKey());
			newMeta.setValue(meta.getValue());
			newMetaList.add(newMeta);
		}
		
		//To Do Expiry method signature change. 
		//TransactionEngineHelper.expiry(callingCircleId, newMetaList);
		
		
		
//	log.warn("Remove Calling Circle Task is not implmented in Notification Workflow");
//		long callingCircleId = 0;
//		for (Meta meta : metas) {
//			if(meta.getKey().equalsIgnoreCase(ScheduleRemoveCallingCircleCmd.CALLING_CIRCLE_ID)) {
//				callingCircleId = Long.valueOf(meta.getValue());
//			}
//		}
//
//		CallingCircleService callingCircleService = NotificationContext.getBean(CallingCircleService.class);
//		callingCircleService.removeCircle(callingCircleId);
		return null;
	}
}
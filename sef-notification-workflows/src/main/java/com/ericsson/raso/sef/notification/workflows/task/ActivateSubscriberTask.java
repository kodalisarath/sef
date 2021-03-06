package com.ericsson.raso.sef.notification.workflows.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.Task;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.sef.scheduler.command.AbortRecycleCommand;

import com.ericsson.sef.scheduler.common.TransactionEngineHelper;

public class ActivateSubscriberTask implements Task<Void> {

	private static Logger log = LoggerFactory.getLogger(ActivateSubscriberTask.class);
	
	private String msisdn;
	
	public ActivateSubscriberTask(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public Void execute() throws SmException {
		log.debug("Executing ActivateSubscriberTask");

		try {
			TransactionEngineHelper.updateSusbcriberContractState(msisdn, ContractState.ACTIVE.name());
			
			AbortRecycleCommand command = new AbortRecycleCommand(msisdn);
			command.execute();
		}  catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new SmException(e);
		}
		log.debug("Executing ActivateSubscriberTask completed");
		return null;
	}
	


}

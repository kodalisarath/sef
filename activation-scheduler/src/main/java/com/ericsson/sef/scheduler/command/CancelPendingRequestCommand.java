package com.ericsson.sef.scheduler.command;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;


public class CancelPendingRequestCommand implements Command<Void> {
	
	private static Logger log = LoggerFactory.getLogger(CancelPendingRequestCommand.class);

	private String jobName;
	
	public CancelPendingRequestCommand(String jobName) {
		this.jobName = jobName;
	}

	@Override
	public Void execute() throws SmException {
		SchedulerService scheduler = SchedulerContext.getSchedulerService();
		try {
			scheduler.deleteJob(jobName);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			throw new SmException(e);
		}
		return null;
	}

}

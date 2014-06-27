package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.CallingCircle;
import com.ericsson.sef.scheduler.RemoveCallingCircleJob;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;


public class ScheduleRemoveCallingCircleCmd implements Command<Void> {

	private static final String REMOVE_CALLING_CIRCLE = "REMOVE_CALLING_CIRCLE";
	public static final String CALLING_CIRCLE_ID = "CALLING_CIRCLE_ID";
	
	private static Logger log = LoggerFactory.getLogger(ScheduleRemoveCallingCircleCmd.class);

	private CallingCircle callingCircle;

	public ScheduleRemoveCallingCircleCmd(CallingCircle callingCircle) {
		this.callingCircle = callingCircle;
	}

	@Override
	public Void execute() throws SmException {
		SchedulerService scheduler = SchedulerContext.getSchedulerService();
		JobDetail job = null;
		Trigger trigger = null;
		try {
			job = newJob(RemoveCallingCircleJob.class).
						withIdentity(REMOVE_CALLING_CIRCLE + callingCircle.getId())
						.usingJobData(CALLING_CIRCLE_ID, callingCircle.getId()).build();

			trigger = newTrigger()
						.withIdentity(REMOVE_CALLING_CIRCLE + callingCircle.getId())
						.startAt(callingCircle.getExpiryTime().toDate()).build();

			scheduler = SchedulerContext.getSchedulerService();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			try {
				scheduler.rescheduleJob(trigger.getKey(), trigger);
			} catch (Exception e1) {
				throw new SmException(e);
			}
		}
		return null;
	}

}

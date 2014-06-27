package com.ericsson.raso.sef.scheduler.test;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.scheduler.ErrorCode;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;
public class TestQuartz implements Command<Void> {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	public Void execute() throws SmException {
		log.debug("Inside TesQuartz Trigger ");
		try {
			JobDetail demoJob = newJob(DemoJob.class).build();
			Trigger trigger = newTrigger()
				    .withIdentity("TestTrigger")
				    .withSchedule(
				      simpleSchedule().withIntervalInSeconds(3)
				        .repeatForever()).build();

			SchedulerService scheduler = SchedulerContext.getSchedulerService();
			scheduler.scheduleJob(demoJob, trigger);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			throw new SmException(ErrorCode.internalServerError);
		}
		
		return null;
	}
}


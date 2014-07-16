package com.ericsson.sef.scheduler.command;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.scheduler.ErrorCode;
import com.ericsson.sef.scheduler.RecycleSubscriberJob;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;


public class RecycleJobCommand implements Command<Void> {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final Subscriber subscriber;
	private final Date executionTime;

	public RecycleJobCommand(Subscriber subscriber, Date executionTime) {
		this.subscriber = subscriber;
		this.executionTime = executionTime;
	}

	@Override
	public Void execute() throws SmException {
		try {
			
			JobDetail job = newJob(RecycleSubscriberJob.class).withIdentity(SchedulerContext.RECYCLE + subscriber.getMsisdn())
																.usingJobData(SchedulerContext.MSISDN, subscriber.getMsisdn()).build();

			Trigger trigger = newTrigger().withIdentity(SchedulerContext.RECYCLE + subscriber.getMsisdn()+"Trigger").startAt(executionTime).build();

			SchedulerService scheduler = SchedulerContext.getSchedulerService();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			throw new SmException(ErrorCode.internalServerError);
		}
		return null;
	}
}

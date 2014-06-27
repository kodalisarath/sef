package com.ericsson.sef.scheduler.command;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.scheduler.ErrorCode;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;
//import com.ericsson.sm.api.subscriber.Subscriber;
//import com.ericsson.sm.core.Command;
//import com.ericsson.sm.core.SmException;

public class RescheduleRecycleCommand implements Command<Void> {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final Subscriber subscriber;
	private final Date executionTime;

	public RescheduleRecycleCommand(Subscriber subscriber, Date executionTime) {
		this.subscriber = subscriber;
		this.executionTime = executionTime;
	}

	@Override
	public Void execute() throws SmException {
		try {
			Trigger trigger = newTrigger().withIdentity(SchedulerContext.RECYCLE + subscriber.getUserId()+"Trigger").startAt(executionTime).build();
			SchedulerService scheduler = SchedulerContext.getSchedulerService();
			scheduler.rescheduleJob(new TriggerKey(SchedulerContext.RECYCLE + subscriber.getUserId()+"Trigger"), trigger);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			throw new SmException(ErrorCode.internalServerError);
		}
		return null;
	}
}

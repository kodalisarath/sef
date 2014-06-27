package com.ericsson.sef.scheduler.command;

import java.util.Arrays;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.scheduler.ErrorCode;
import com.ericsson.sef.scheduler.SchedulerContext;
import com.ericsson.sef.scheduler.SchedulerService;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;
//import com.ericsson.sm.api.subscriber.Subscriber;
//import com.ericsson.sm.core.Command;
//import com.ericsson.sm.core.SmException;

public class AbortRecycleCommand implements Command<Void> {
	
	private static Logger log = LoggerFactory.getLogger(AbortRecycleCommand.class);

	private final String msisdn;
	
	public AbortRecycleCommand(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public Void execute() throws SmException {
		SchedulerService scheduler = SchedulerContext.getSchedulerService();
		SubscriberInfo subscriberInfo = TransactionEngineHelper.getSubscriberInfo(msisdn);
		Subscriber subscriber = subscriberInfo.getSubscriber();
		try {
			scheduler.deleteJobs(Arrays.asList(new JobKey(SchedulerContext.RECYCLE + subscriber.getUserId())));
		} catch (SchedulerException e) {
			log.error("Error deleting Recycle msisdn job.", e);
			throw new SmException("", ErrorCode.internalServerError, e);
		}
		return null;
	}
}

package com.ericsson.sef.scheduler;

import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.transaction.annotation.Transactional;

public class TransactionalSchedulerService implements SchedulerService {

	private SefScheduler scheduler;
	
	public TransactionalSchedulerService(SefScheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	@Transactional
	public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		return scheduler.getDelegate().scheduleJob(jobDetail, trigger);
	}

	@Override
	@Transactional
	public Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) throws SchedulerException {
		return scheduler.getDelegate().rescheduleJob(triggerKey, newTrigger);
	}

	@Override
	@Transactional
	public boolean deleteJobs(List<JobKey> jobKeys) throws SchedulerException {
		return scheduler.getDelegate().deleteJobs(jobKeys);
	}

	@Override
	public boolean deleteJob(String jobId) throws SchedulerException {
		return scheduler.getDelegate().deleteJob(new JobKey(jobId));
	}
}

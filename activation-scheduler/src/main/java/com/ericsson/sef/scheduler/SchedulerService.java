package com.ericsson.sef.scheduler;

import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public interface SchedulerService {

	Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException;

	Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) throws SchedulerException;

	boolean deleteJobs(List<JobKey> jobKeys) throws SchedulerException;
	
	boolean deleteJob(String jobId) throws SchedulerException;

}

package com.ericsson.raso.sef.scheduler.test;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class DemoJob  implements Job{
	private Logger log = LoggerFactory.getLogger(this.getClass());
 public void execute(JobExecutionContext context) throws JobExecutionException {
   
  JobKey jobKey = context.getJobDetail().getKey();
   log.debug("Hi This is a demo job "+jobKey);
 }
 
}
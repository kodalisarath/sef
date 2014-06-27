package com.ericsson.sef.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.ne.NotificationWorkflowService;
import com.ericsson.sef.scheduler.command.ScheduleRemoveCallingCircleCmd;
/*import com.ericsson.sm.api.notification.Meta;
import com.ericsson.sm.api.notification.NotificationWorkflowService;
import com.ericsson.sm.core.config.CallingCircleCfg;
import com.ericsson.sm.core.config.IConfig;
*/
public class RemoveCallingCircleJob implements Job {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
	//To Do Once Notification Workflows is up.
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		log.info("RemoveCallingCircleJob executing job Id : "+ context.getJobDetail().getKey().getName() + 
				" Job Details  : " + jobDataMap.toString());
		
		try {
			NotificationWorkflowService service = SchedulerContext.getCallingCircleWorkflow();
			Long callingCircleId = jobDataMap.getLong(ScheduleRemoveCallingCircleCmd.CALLING_CIRCLE_ID);
			
			
			List<Meta> list = new ArrayList<Meta>();
			list.add(new Meta(ScheduleRemoveCallingCircleCmd.CALLING_CIRCLE_ID,String.valueOf(callingCircleId)));
			list.add(new Meta("ACTION", "REMOVE"));
			
			//CallingCircleCfg callingCircleCfg = SchedulerContext.getBean(IConfig.class).getDelegate().getCallingCircle();
			String eventId = SchedulerContext.getBean(IConfig.class).getValue("activationScheduler", "eventId");

			service.processWorkflow(null, eventId, list);

		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}

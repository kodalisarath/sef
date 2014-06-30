package com.ericsson.sef.scheduler;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.core.db.mapper.ScheduledRequestMapper;
import com.ericsson.raso.sef.core.ne.NotificationWorkflowService;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;



public class SchedulerContext implements ApplicationContextAware {
	
	public static final String MSISDN = "MSISDN";
	public static final String RECYCLE = "RECYLCE";

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SchedulerContext.context = applicationContext;
	}
	
	public static CamelContext getCamelContext() {
		return context.getBean("com.ericsson.sef.schedluer", CamelContext.class);
	}
	
	/*public static ScheduledRequestMapper getScheduledRequestMapper() {
		return context.getBean(ScheduledRequestMapper.class);
	}*/
	
	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}
	
	public static SchedulerService getSchedulerService() {
		return context.getBean(SchedulerService.class);
	}
	
	public static NotificationWorkflowService getCallingCircleWorkflow() {
		return context.getBean("callingCircleWorkflow", NotificationWorkflowService.class);
	}
	
	public static DataSource getDataSource() {
		return context.getBean("dataSource", DataSource.class);
	}
	
	public static ISubscriptionRequest getSubscriptionRequest() {
		return context.getBean(ISubscriptionRequest.class);
	}

	public static ISubscriberRequest getSubscriberRequest() {
		return context.getBean(ISubscriberRequest.class);
	}
}

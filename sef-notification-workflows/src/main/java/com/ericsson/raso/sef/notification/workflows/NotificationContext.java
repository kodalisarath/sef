package com.ericsson.raso.sef.notification.workflows;

import org.apache.camel.CamelContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class NotificationContext implements ApplicationContextAware {

	private static ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		NotificationContext.context = applicationContext;
	}
	
	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}
	
	public static <T> T getBean(String name, Class<T> type) {
		return context.getBean(name, type);
	}
	public static CamelContext getCamelContext() {
		return context.getBean("com.ericsson.sm.notifWorkflows", CamelContext.class);
	}
}

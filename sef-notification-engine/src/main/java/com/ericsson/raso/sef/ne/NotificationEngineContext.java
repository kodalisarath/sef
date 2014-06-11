/*package com.ericsson.raso.sef.ne;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.core.config.IConfig;

public class NotificationEngineContext implements ApplicationContextAware {

	private static ApplicationContext context;
	
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		NotificationEngineContext.context = applicationContext;
	}
	
	public static SubscriberManagement getSubscriberManagement() {
		return context.getBean(SubscriberManagement.class);
	}
	
	public static String getComponentName() {
		return "notification-engine";
	}
	
	public static String getSubscriberCmpName() {
		return "subscriber-frontend";
	}
	
	public static IConfig getConfig() {
		return context.getBean(IConfig.class);
	}
	
	public static SmppClientFactory getSmppClientFactory() {
		return context.getBean(SmppClientFactory.class);
	}
	
	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}
	
	public static <T> T getBean(String name, Class<T> type) {
		return context.getBean(name, type);
	}
	
}
*/
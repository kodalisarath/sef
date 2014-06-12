package com.ericsson.raso.sef.ne;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class NotificationEngineServiceResolver implements ApplicationContextAware {

	private static ApplicationContext context;
	
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		NotificationEngineServiceResolver.context = applicationContext;
	}
	
	
	
	
	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}
	
	public static <T> T getBean(String name, Class<T> type) {
		return context.getBean(name, type);
	}
	
}

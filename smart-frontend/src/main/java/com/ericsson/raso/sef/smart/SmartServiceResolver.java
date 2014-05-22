package com.ericsson.raso.sef.smart;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SmartServiceResolver implements ApplicationContextAware {

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		SmartServiceResolver.context = arg0;
	}

	
	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}
}

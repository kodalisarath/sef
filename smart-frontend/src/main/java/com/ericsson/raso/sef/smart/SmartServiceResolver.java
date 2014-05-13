package com.ericsson.raso.sef.smart;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.sef.bes.api.subscriber.SubscriberManagement;
import com.ericsson.sef.bes.api.subscription.SubscriptionManagement;

public class SmartServiceResolver implements ApplicationContextAware {

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		SmartServiceResolver.context = arg0;
	}

	
	public static SubscriberManagement getSubscriberManagement() {
		return SmartServiceResolver.context.getBean(SubscriberManagement.class);
	}

	public static SubscriptionManagement getSubscriptionManagement() {
		return SmartServiceResolver.context.getBean(SubscriptionManagement.class);
	}
	
	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}
}

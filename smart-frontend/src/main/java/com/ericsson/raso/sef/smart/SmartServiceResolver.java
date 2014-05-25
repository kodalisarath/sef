package com.ericsson.raso.sef.smart;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;

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
	
	public static ISubscriptionRequest getSubscriptionRequest() {
		return context.getBean(ISubscriptionRequest.class);
	}
	
	public static ISubscriberRequest getSubscriberRequest() {
		return context.getBean(ISubscriberRequest.class);
	}
	
}

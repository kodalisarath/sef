package com.ericsson.raso.sef.fulfillment.commons;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.fulfillment.profiles.IProfileRegistry;
import com.ericsson.sef.bes.api.fulfillment.FulfillmentResponse;

public class FulfillmentServiceResolver implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}
	
	public static IServiceRegistry getServiceRegistry() {
		return context.getBean(IServiceRegistry.class);
	}
	
	public static FulfillmentResponse getFulfillmentResponseClient() {
		return context.getBean(FulfillmentResponse.class);
	}
	
	public static IProfileRegistry getProfileRegistry() {
		return context.getBean(IProfileRegistry.class);
	}
	
	
}

package com.ericsson.raso.sef.ne.core.smpp;

import org.apache.camel.CamelContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GatewayContext implements ApplicationContextAware {

	private static ApplicationContext context;
	
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		GatewayContext.context = applicationContext;
	}
	
	public static CamelContext getCamelContext() {
		return context.getBean("com.ericsson.sm.smppgateway", CamelContext.class);
	}
}

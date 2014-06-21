package com.ericsson.raso.sef.charginggateway;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CgContext implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CgContext.context = applicationContext;
	}
	
	public static String getComponentName() {
		return "charging-gateway";
	}
	
	/*public static IConfig getConfig() {
		return context.getBean(IConfig.class);
	}
	
	public static SmCache getSmCache() {
		return context.getBean(SmCache.class);
	}*/
}

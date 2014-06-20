package com.ericsson.sef.chargingadapter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


//import com.ericsson.sm.core.config.IConfig;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
//import com.ericsson.sm.core.diameter.ScapChargingApi;
import com.ericsson.sef.diameter.ScapChargingApi;

public class CaContext implements ApplicationContextAware {

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CaContext.context = applicationContext;
	}
	/*
	public static IConfig getConfig() {
		return context.getBean(IConfig.class);
	}
	*/
	public static ScapChargingApi getChargingApi() {
		return context.getBean(ScapChargingApi.class);
	}
	
	public static SubscriberService getSubscriberService() {
		return context.getBean(SubscriberService.class);
	}

	public static String getComponentName() {
		return "charging-adapter";
	}
	
	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}
}

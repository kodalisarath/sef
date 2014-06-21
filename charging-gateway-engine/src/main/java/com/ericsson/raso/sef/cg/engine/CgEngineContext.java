package com.ericsson.raso.sef.cg.engine;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.cg.engine.util.ScapChargingApi;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;

public class CgEngineContext implements ApplicationContextAware {

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CgEngineContext.context = applicationContext;
	}
	
	public static ScapChargingApi getChargingApi() {
		return context.getBean(ScapChargingApi.class);
	}
	
	public static IConfig getConfig() {
		return context.getBean(IConfig.class);
	}
	
	public static String getComponentName() {
		return "charging-gateway";
	}
	
	/*public static IpcCluster getIpcCluster() {
		return context.getBean(IpcCluster.class);
	}
	*/
/*	public static SubscriberManagement getSubscriberManagement() {
		return context.getBean(SubscriberManagement.class);
	}
	
	public static SubscriberService getSubscriberService() {
		return context.getBean(SubscriberService.class);
	}

	public static SmartCommonService getSmartCommonService() {
		return context.getBean(SmartCommonService.class);
	}
	*/
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

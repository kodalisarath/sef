package com.ericsson.raso.sef.bes.prodcat;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.core.config.IConfig;

public class ServiceResolver implements ApplicationContextAware {

	public static ApplicationContext context = null; 

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		ServiceResolver.context = context;
	}
	
	public static IOfferCatalog getOfferCatalog() {
		return context.getBean(IOfferCatalog.class);
	}
	
	public static ServiceRegistry getServiceRegistry() {
		return context.getBean(ServiceRegistry.class);
	}
	
	public static IConfig getConfig() {
		return context.getBean(IConfig.class);
	}

}

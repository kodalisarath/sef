package com.ericsson.raso.sef.bes.prodcat;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;

public class ServiceResolver implements ApplicationContextAware {

	public static ApplicationContext context = null; 

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		ServiceResolver.context = context;
	}
	
	public static IOfferCatalog getOfferCatalog() {
		return context.getBean(IOfferCatalog.class);
	}

}

package com.ericsson.sm.client.af;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.sm.client.af.internal.AccountFinderRoute;

public class DnsServiceResolver implements ApplicationContextAware {

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		DnsServiceResolver.context = applicationContext;
	}
	
	public static IConfig getConfig() {
		return context.getBean(IConfig.class);
	}
	
	public static AccountFinderRoute getAccountFinderRoute() {
		return context.getBean(AccountFinderRoute.class);
	}
}

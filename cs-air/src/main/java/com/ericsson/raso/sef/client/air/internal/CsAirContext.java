package com.ericsson.raso.sef.client.air.internal;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.client.air.AirClient;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcClientFactory;

public class CsAirContext implements ApplicationContextAware {

	private static ApplicationContext context;
	private static final String csConfigSectionName = "CS-AIR";
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CsAirContext.context = applicationContext;
	}
	
	public static AirClient getAirClient() {
		return context.getBean(AirClient.class);
	}
	
	public static String getSection() {
		return "cs-air";
	}
	
	public static XmlRpcClientFactory getXmlRpcClientFactory() {
		return context.getBean(XmlRpcClientFactory.class);
	}
	
	public static IConfig getConfig() {
		return context.getBean(IConfig.class);
	}
	
	public static String getProperty(String key) {
		IConfig config = context.getBean(IConfig.class);
		return config.getValue(getSection(), key);
	}
}

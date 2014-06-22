package com.ericsson.raso.sef.core.config;

import java.util.ArrayList;
import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

public class ComponentPropertiesBean implements FactoryBean<Properties> {
	
	private IConfig config;
	private String component;
	
	public ComponentPropertiesBean(IConfig config, String component) {
		this.config = config;
		this.component = component;
	}

	@Override
	public Properties getObject() throws Exception {
		Section section = config.getSection(component);
		if ( section == null)
			throw new IllegalArgumentException(String.format("%s is not configured in the config.xml", component));
		ArrayList<Property> sectionProperties = section.getProperty();
		Properties properties = new Properties();
		for (Property property : sectionProperties) {
			properties.put(property.getKey(), property.getValue());
		}
		return properties;
	}

	@Override
	public Class<?> getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}

package com.ericsson.sef.scheduler.config;

import java.util.List;

public class Property {

	private List<String> values;
	private List<Property> entries;
	private String name;
	private String value;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public List<Property> getEntries() {
		return entries;
	}

	public void setEntries(List<Property> entries) {
		this.entries = entries;
	}
}


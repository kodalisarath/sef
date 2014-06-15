package com.ericsson.raso.sef.core.config;

import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name="router")
public class Router {
	
	Ranges ranges;
	Values values;
	
	@XmlElement(name="ranges")
	public Ranges getRanges() {
		return ranges;
	}
	
	public void setRanges(Ranges ranges) {
		this.ranges = ranges;
	}
	
	@XmlElement(name="values")
	public Values getValues() {
		return values;
	}
	
	public void setValues(Values values) {
		this.values = values;
	}
	
	
	
	

}

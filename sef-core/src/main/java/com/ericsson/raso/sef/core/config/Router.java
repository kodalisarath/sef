package com.ericsson.raso.sef.core.config;

import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name="router")
public class Router {
	
	Ranges ranges;
	Values values;
	
	public Ranges getRanges() {
		return ranges;
	}
	
	@XmlElement(name="ranges")
	public void setRanges(Ranges ranges) {
		this.ranges = ranges;
	}
	
	public Values getValues() {
		return values;
	}
	
	@XmlElement(name="values")
	public void setValues(Values values) {
		this.values = values;
	}
	
	
	
	

}

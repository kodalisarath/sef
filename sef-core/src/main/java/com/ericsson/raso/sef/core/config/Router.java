package com.ericsson.raso.sef.core.config;

public class Router {
	
	Ranges ranges;
	Values values;
	
	public Ranges getRanges() {
		return ranges;
	}
	
	public void setRanges(Ranges ranges) {
		this.ranges = ranges;
	}
	
	public Values getValues() {
		return values;
	}
	
	public void setValues(Values values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Router [ranges=" + ranges + ", values=" + values + "]";
	}
	
	
	
	

}

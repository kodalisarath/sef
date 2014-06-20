package com.ericsson.raso.sef.core.config;

import java.util.ArrayList;
public class Ranges {

	private ArrayList<Range> range;

	public ArrayList<Range> getRange() {
		return range;
	}
	
	public void setRange(ArrayList<Range> range) {
		this.range = range;
	}

	@Override
	public String toString() {
		return "Ranges [range=" + range + "]";
	}
	
	
}

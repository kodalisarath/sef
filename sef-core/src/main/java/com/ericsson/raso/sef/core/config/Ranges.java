package com.ericsson.raso.sef.core.config;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ranges")
public class Ranges {

	private ArrayList<Range> range;

	@XmlElement(name="range")
	public ArrayList<Range> getRange() {
		return range;
	}
	
	public void setRange(ArrayList<Range> range) {
		this.range = range;
	}
	
	
}

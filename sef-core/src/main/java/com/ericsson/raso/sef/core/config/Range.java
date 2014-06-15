package com.ericsson.raso.sef.core.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="range")
public class Range {
	
	private long start;
	private long end;
	private Value value;
	
	public long getStart() {
		return start;
	}
	
	@XmlAttribute
	public void setStart(long start) {
		this.start = start;
	}
	
	public long getEnd() {
		return end;
	}
	
	@XmlAttribute
	public void setEnd(long end) {
		this.end = end;
	}
	
	public Value getValue() {
		return value;
	}
	
	@XmlAttribute
	public void setValue(Value value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Range [start=" + start + ", end=" + end + ", value=" + value + "]";
	}
	
	

}

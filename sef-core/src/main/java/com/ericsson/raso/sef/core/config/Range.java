package com.ericsson.raso.sef.core.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="range")
public class Range implements Comparable<Range> {
	
	private long start;
	private long end;
	private String value;
	
	@XmlAttribute
	public long getStart() {
		return start;
	}
	
	public void setStart(long start) {
		this.start = start;
	}
	
	@XmlAttribute
	public long getEnd() {
		return end;
	}
	
	public void setEnd(long end) {
		this.end = end;
	}
	
	@XmlAttribute
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Range [start=" + start + ", end=" + end + ", value=" + value + "]";
	}

	@Override
	public int compareTo(Range o) {
		return (int) ((this.start - o.start) & Integer.MAX_VALUE);
	}

	public boolean contains(long searched) {
		if (searched >= this.start && searched <= this.end)
			return true;
		return false;
	}
	
	

}

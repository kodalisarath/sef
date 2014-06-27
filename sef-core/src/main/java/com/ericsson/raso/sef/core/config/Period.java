package com.ericsson.raso.sef.core.config;

import java.io.Serializable;

public class Period implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int value = 0;
	private PeriodUnit periodUnit;

	public Period(int value, PeriodUnit periodUnit) {
		this.value = value;
		this.periodUnit = periodUnit;
	}
	
	public PeriodUnit getPeriodUnit() {
		return periodUnit;
	}
	
	public int getValue() {
		return value;
	}
	
	public long getPeriodInMills() {
		return value * periodUnit.getMilliseconds();
	}
	
	public static Period valueOf(String str) {
		String[] p = str.split("-");
		return new Period(Integer.valueOf(p[0]), PeriodUnit.valueOf(p[1]));
	}
	
	@Override
	public String toString() {
		return value + "-" + periodUnit.name();
	}
	
}

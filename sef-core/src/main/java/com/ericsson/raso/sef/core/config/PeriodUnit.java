package com.ericsson.raso.sef.core.config;

public enum PeriodUnit {
	
	DAY(86400000), HOUR(3600000), SECOND (1000), MINUTE(60000);
	
	private long milliseconds;

	private PeriodUnit(long milliseconds) {
		this.milliseconds = milliseconds;
	}
	
	public long getMilliseconds() {
		return milliseconds;
	}

}

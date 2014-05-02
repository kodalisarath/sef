package com.ericsson.raso.sef.watergate;

public enum TimeUnit {
	
	SECONDS(1000),
	MINS(60000),
	HOUR(3600000),
	DAY(86400000);

	private long milliseconds;
	
	private TimeUnit(long milliseconds) {
		this.milliseconds = milliseconds;
	}

	public long getMilliseconds() {
		return milliseconds;
	}
}

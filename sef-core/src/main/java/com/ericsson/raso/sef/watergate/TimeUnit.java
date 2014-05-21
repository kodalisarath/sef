package com.ericsson.raso.sef.watergate;

public enum TimeUnit {

	SECONDS,
	MINS,
	HOUR,
	DAY;

	public long longValue() {

		switch (this) {
			case SECONDS:
				return 1000;
			case DAY:
				return 86400000;
			case HOUR:
				return 3600000;
			case MINS:
				return 60000;
			default:
				return 0;
		}
	}
}

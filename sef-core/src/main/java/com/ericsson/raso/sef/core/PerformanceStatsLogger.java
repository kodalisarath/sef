package com.ericsson.raso.sef.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceStatsLogger {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("perf-stats");
	
	public static void log(String component, long timeTaken) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(System.currentTimeMillis() + "," + component + "," + RequestContextLocalStore.get().getRequestId() +"," + timeTaken);
		}
	}
	
	public static void log(String component, long timeTaken, String requestId) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(System.currentTimeMillis() + "," + component + "," + requestId +"," + timeTaken);
		}
	}
}

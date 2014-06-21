package com.ericsson.raso.sef.cg.engine.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.core.PerformanceStatsLogger;

public class RequestExitProcessor implements Processor {
	
	@Override
	public void process(Exchange exchange) throws Exception {
		PerformanceStatsLogger.log("CG-ENGINE", (System.currentTimeMillis() - (Long) exchange.getIn().getHeader("stopwatch")));
	}
}

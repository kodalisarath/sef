package com.ericsson.raso.sef.notification.workflows.core;

import org.apache.camel.Exchange;

import com.ericsson.raso.sef.core.PerformanceStatsLogger;
import com.ericsson.raso.sef.notification.workflows.core.RequestContextCallback; 

@SuppressWarnings("unchecked")
public class ExchangeUtil {
	
	private static final String REQUEST_CONTEXT_MAP = "REQUEST_CONTEXT_MAP";
	
	public static <T> T getValue(Exchange exchange, String key) {
		T value = (T) exchange.getIn().getHeader(key);
		if(value != null) {
			return value;
		}
		
		if(value == null) {
			RequestContextMap requestContextMap = (RequestContextMap) exchange.getIn().getHeader(REQUEST_CONTEXT_MAP);
			if(requestContextMap != null) {
				value = (T) requestContextMap.get(key);
			}
		}
		
		return value;
	}
	
	public static void setValue(Exchange exchange, String key, Object value) {
		exchange.getIn().setHeader(key, value);
	}
	
	public static void setRequestContextMap(Exchange exchange, RequestContextMap requestContextMap) {
		exchange.getIn().setHeader(REQUEST_CONTEXT_MAP, requestContextMap);
	}
	
	public static RequestContextMap getRequestContextMap(Exchange exchange) {
		return (RequestContextMap) exchange.getIn().getHeader(REQUEST_CONTEXT_MAP);
	}
	
	public static void putInRequestContext(Exchange exchange, String key, Object value, RequestContextCallback callback) {
		RequestContextMap requestContextMap = (RequestContextMap) exchange.getIn().getHeader(REQUEST_CONTEXT_MAP);
		if(requestContextMap == null) {
			requestContextMap = callback.get();
			setRequestContextMap(exchange, requestContextMap);
		}
		requestContextMap.put(key, value);
	}
	
	public static void clearExchange(Exchange  exchange) {
		exchange.getIn().getHeaders().clear();
	}
	
	public static void logStat(String component, Exchange exchange) {
		Long startTime = (Long) exchange.getIn().getHeader("stopwatch");
		if(startTime != null) {
			PerformanceStatsLogger.log(component, System.currentTimeMillis() - startTime);
		}
	}
}

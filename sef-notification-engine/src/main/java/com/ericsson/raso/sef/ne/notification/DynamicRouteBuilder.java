package com.ericsson.raso.sef.ne.notification;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

public abstract class DynamicRouteBuilder {

	private CamelContext camelContext;

	public DynamicRouteBuilder(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

	public abstract Map<String, RouteBuilder> createRoutes();

	public void start() throws Exception {
		Map<String, RouteBuilder> routes = createRoutes();
		for (Map.Entry<String, RouteBuilder> entry : routes.entrySet()) {
			camelContext.addRoutes(entry.getValue());
		}
		for (Map.Entry<String, RouteBuilder> entry : routes.entrySet()) {
			camelContext.startRoute(entry.getKey());
		}
	}
}

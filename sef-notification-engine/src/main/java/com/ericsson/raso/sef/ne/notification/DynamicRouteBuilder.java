package com.ericsson.raso.sef.ne.notification;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class DynamicRouteBuilder  {

	private CamelContext camelContext;
	private static Logger log = LoggerFactory.getLogger(DynamicRouteBuilder.class);
	public DynamicRouteBuilder(CamelContext camelContext) {
		this.camelContext = camelContext;
//	ActiveMQComponent.activeMQComponent("tcp://localhost:61616");
	}
	
	

	public abstract Map<String, RouteBuilder> createRoutes();

	public void start() throws Exception {
		Map<String, RouteBuilder> routes = createRoutes();
		log.debug("Routes are "+routes);
		for (Map.Entry<String, RouteBuilder> entry : routes.entrySet()) {
			camelContext.addRoutes(entry.getValue());
		}
		for (Map.Entry<String, RouteBuilder> entry : routes.entrySet()) {
			camelContext.startRoute(entry.getKey());
		}
	}
}

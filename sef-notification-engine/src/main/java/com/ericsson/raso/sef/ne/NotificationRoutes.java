package com.ericsson.raso.sef.ne;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.LoadBalanceDefinition;

import com.ericsson.raso.ne.core.smpp.SmppClient;
import com.ericsson.raso.ne.core.smpp.SmppClientFactory;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.config.Property;
import com.ericsson.raso.sef.ne.notification.DynamicRouteBuilder;

public class NotificationRoutes extends DynamicRouteBuilder {

	private final IConfig config;

	public NotificationRoutes(CamelContext camelContext, IConfig config) {
		super(camelContext);
		this.config = config;
	}

	
	public Map<String, RouteBuilder> createRoutes() {
		Map<String, RouteBuilder> routes = new LinkedHashMap<String, RouteBuilder>();
		
		routes.put("jmsNotifRoute",new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("activemq:queue:notification").bean(NotificationMessageProcessor.class).routeId("jmsNotifRoute");
			}
		});
		
		SmppClientFactory factory = null;//NotificationEngineContext.getSmppClientFactory();

		//Property property = config.getProperty(NotificationEngineContext.getComponentName(), "smscClients");

		final List<String> smscEndPoints = null;
	
		for (String smscEndpoint : smscEndPoints) {
			SmppClient smppClient = factory.create(smscEndpoint);
			routes.put(smscEndpoint, createSmppClientRoute(smscEndpoint, smppClient));
		}

		routes.put("smppLoadBalence", createSmppLoadBalncer(smscEndPoints));
		return routes;
	}

	private RouteBuilder createSmppLoadBalncer(final List<String> smscEndPoints) {
		return new RouteBuilder() {
			public void configure() throws Exception {
				LoadBalanceDefinition definition = from("direct:smppLoadBalence").routeId("smppLoadBalence").loadBalance().roundRobin();
				for (String endpoint : smscEndPoints) {
					definition.to("direct:" + endpoint);
				}
			}
		};
	}

	private RouteBuilder createSmppClientRoute(final String endpointId, final SmppClient client) {
		return new RouteBuilder() {
			public void configure() throws Exception {
				from("direct:" + endpointId)
						.errorHandler(defaultErrorHandler().maximumRedeliveries(3).maximumRedeliveryDelay(5)).process(new Processor() {

							@SuppressWarnings("unchecked")
							public void process(Exchange exchange) throws Exception {
								String msisdn = (String) exchange.getIn().getHeader("msisdn");
								Object msg = exchange.getIn().getHeader("message");
								String senderAddr = (String) exchange.getIn().getHeader("senderAddr");
								if(msg instanceof String) {
									client.sendMessage(msisdn, ((String)msg).trim(), senderAddr);
								} else {
									for (String message : (List<String>)msg) {
										client.sendMessage(msisdn, message.trim(), senderAddr);
									}
								}
							}
				}).routeId(endpointId);
			}
		};
	}
}

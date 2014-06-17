package com.ericsson.raso.sef.ne;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.LoadBalanceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.config.Property;
import com.ericsson.raso.sef.core.config.Section;
import com.ericsson.raso.sef.ne.core.smpp.SmppClient;
import com.ericsson.raso.sef.ne.core.smpp.SmppClientFactory;
import com.ericsson.raso.sef.ne.core.smpp.internal.CloudhopperSmppClient;
import com.ericsson.raso.sef.ne.core.smpp.internal.DefaultSmppClientFactory;
import com.ericsson.raso.sef.ne.core.smpp.internal.JSmppClientFactory;
import com.ericsson.raso.sef.ne.notification.DynamicRouteBuilder;

public class NotificationRoutes extends DynamicRouteBuilder {
private static Logger logger = LoggerFactory.getLogger(NotificationRoutes.class);
/*	private final IConfig config;

	public NotificationRoutes(CamelContext camelContext, IConfig config) {
		super(camelContext);
		this.config = config;
	}
*/

public NotificationRoutes(CamelContext camelContext) {
	super(camelContext);
	
}
	
	
	public Map<String, RouteBuilder> createRoutes() {
		Map<String, RouteBuilder> routes = new LinkedHashMap<String, RouteBuilder>();
		
		logger.debug("jmsNotifRoute abount to start");
		routes.put("jmsNotifRoute",new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("activemq:queue:notification").bean(NotificationMessageProcessor.class).routeId("jmsNotifRoute");
			}
		});
		logger.debug("jmsNotifRoute completed");
		SmppClientFactory factory = new DefaultSmppClientFactory();

		logger.debug("DefaultSmppClientFactory successful");		//Property property = config.getProperty(NotificationEngineContext.getComponentName(), "smscClients");

		IConfig config = SefCoreServiceResolver.getConfigService();
		Section section = config.getSection("Global_smscClients");
		List<Property> propertyList = SefCoreServiceResolver.getConfigService().getProperties(section);
	
		final List<String> smscEndPoints = new ArrayList<String>();
	
		for (Property smscEndpoint : propertyList) {
			SmppClient smppClient = factory.create(smscEndpoint.getKey());
			routes.put(smscEndpoint.getKey(), createSmppClientRoute(smscEndpoint.getKey(), smppClient));
			smscEndPoints.add(smscEndpoint.getKey());
	}

		routes.put("smppLoadBalence", createSmppLoadBalncer(smscEndPoints));
		
		logger.debug("All Routes successful and returning");
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
				logger.debug("Inside createSmppClientRoute....endpointId is "+endpointId);

				from("direct:" + endpointId)
						.errorHandler(defaultErrorHandler().maximumRedeliveries(3).maximumRedeliveryDelay(5)).process(new Processor() {

							@SuppressWarnings("unchecked")
							public void process(Exchange exchange) throws Exception {
								
								String msisdn = (String) exchange.getIn().getHeader("msisdn");
								
								Object msg = exchange.getIn().getHeader("message");
								logger.debug("Inside ProcessMessgae...Message Received Here..msisdn is."+msisdn  +" message is "+msg);
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

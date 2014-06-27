package com.ericsson.raso.sef.ne;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.opensaml.ws.WSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ne.ExternalNotifcationEvent;
import com.ericsson.raso.sef.core.ne.ExternalNotificationTemplateCatalog;
import com.ericsson.raso.sef.core.ne.NotificationAction;
import com.ericsson.raso.sef.core.ne.NotificationMessage;
import com.ericsson.raso.sef.core.ne.NotificationWorkflowService;
import com.ericsson.raso.sef.core.ne.StringUtils;
import com.ericsson.raso.sef.core.smpp.SmppMessage;


public class NotificationMessageProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public void process(final Exchange exchange) throws Exception {
		log.info(String.format("INCOMING MSG", exchange));
		ExternalNotificationTemplateCatalog catalog = NotificationEngineServiceResolver
				.getBean(ExternalNotificationTemplateCatalog.class);
		Object body = exchange.getIn().getBody();

		if (body instanceof SmppMessage) {
			SmsParser parser = new DefaultSmsParser((SmppMessage) body);
			ExternalNotifcationEvent event = null;
			NotificationAction action = NotificationAction.ASIS;
			String senderAddr = null;
			if (parser.isValidSm()) {
				event = catalog.getEventById(parser.getEventId());
				if (event == null) {
					log.error("Event with eventID: " + parser.getEventId() + " is not configured in the IL.");
					return;
				}
				 
				action = event.getAction();
				senderAddr = event.getSenderAddr();

				switch (action) {
				case ASIS:
					sendMessage(parser.getMsisdn(), parser.getMessages(), senderAddr, exchange);
					break;
				case MASSAGE:
					sendMessage(parser.getMsisdn(), parser.getMetas(), event, exchange);
					break;
				case WORKFLOW:
					sendMessage(parser.getMsisdn(), parser.getMetas(), event, exchange);
					executeWorkflow(parser, event);
					break;
				default:
					break;
				}

				if (event != null && event.getChargeAmount() != null && event.getChargeAmount().getAmount() > 0) {
					
					ChargeAmountTask chargeAmountTask = new ChargeAmountTask(parser.getMsisdn(),
							event.getChargeAmount());
					chargeAmountTask.execute();
				} else {
					log.debug("Charging is not required for event  "+event);
				}

			} else {
				log.debug("Invalid message: {}", body.toString());
				
			}
		} else {
			System.out.println("This needs to be removed - Before GO LIVE.. (NotificationMessageProcessor). ");
			
//			log.debug("Scheduler -About to Initalize. ");
//			TestQuartz testQuartz = new TestQuartz();
//			log.debug("Scheduler -About to Start Execute method. ");
//			testQuartz.execute();
//			log.debug("This needs to be removed - Scheduler Executed. ");
//			log.debug("Unrecognized message format of type [{}].", body.getClass().getName());
		}
	} 

	private void sendMessage(String msisdn, List<Meta> metas, ExternalNotifcationEvent event, Exchange exchange) {
		if (event.getMessages() != null && event.getMessages().size() > 0) {
			List<String> messages = new ArrayList<String>();
			for (NotificationMessage msg : event.getMessages()) {
				messages.add(StringUtils.prepareMessage(msg.getMessage(), metas));
			}
			sendMessage(msisdn, messages, event.getSenderAddr(), exchange);
		}
	}

	private void sendMessage(String msisdn, List<String> messages, String senderAddr, Exchange exchange) {
		if (messages != null && messages.size() > 0) {
			ProducerTemplate template = exchange.getContext().createProducerTemplate();
			template.setDefaultEndpointUri("direct:smppLoadBalence");
			exchange.getIn().setHeader("msisdn", msisdn);
			exchange.getIn().setHeader("senderAddr", senderAddr);
			exchange.getIn().setHeader("message", messages);
			template.send(exchange);
		}
	}

	private void executeWorkflow(SmsParser parser, ExternalNotifcationEvent event) throws WSException {
		log.debug(String.format("Executing workflow for Workflow Id [%s]", event.getWsClientId()));
		/*
		 * NotificationWorkflowService service =
		 * NotificationEngineContext.getBean(event.getWsClientId(),
		 * NotificationWorkflowService.class);
		 */

		NotificationWorkflowService service = NotificationEngineServiceResolver.getBean(event.getWsClientId(),
				NotificationWorkflowService.class);
		service.processWorkflow(parser.getMsisdn(), event.getEventId(), toNotificationMeta(parser.getMetas()));
	}

	private List<Meta> toNotificationMeta(List<Meta> metas) {
		List<Meta> mlist = new ArrayList<Meta>();
		if (metas != null) {
			for (Meta m : metas) {
				mlist.add(new Meta(m.getKey(), m.getValue()));
			}
		}
		return mlist;
	}
}

package com.ericsson.raso.sef.ne;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.opensaml.ws.WSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.ne.core.smpp.SmppMessage;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.ne.notification.ExternalNotifcationEvent;
import com.ericsson.raso.sef.ne.notification.ExternalNotificationTemplateCatalog;
import com.ericsson.raso.sef.ne.notification.NotificationAction;
import com.ericsson.raso.sef.ne.notification.NotificationMessage;
import com.ericsson.raso.sef.ne.notification.NotificationWorkflowService;
import com.ericsson.raso.sef.ne.notification.StringUtils;

public class NotificationMessageProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	
	public void process(final Exchange exchange) throws Exception {
		
		ExternalNotificationTemplateCatalog catalog = NotificationEngineServiceResolver.getBean(ExternalNotificationTemplateCatalog.class);
		
		//ExternalNotificationTemplateCatalog catalog = null;
		Object body = exchange.getIn().getBody();
		if (body instanceof SmppMessage) {
			SmsParser parser = new DefaultSmsParser((SmppMessage) body);

			ExternalNotifcationEvent event = null;
			NotificationAction action = NotificationAction.ASIS;
			String senderAddr = null;
			if (parser.isValidSm()) {
				event = catalog.getEventById(parser.getEventId());
				if (event == null) {
					log.error("Event with eventID: " + parser.getEventId()
							+ " is not configured in the IL.");
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
				
				if(event != null && event.getChargeAmount() != null && event.getChargeAmount().getAmount() > 0) {
					ChargeAmountTask chargeAmountTask = new ChargeAmountTask(parser.getMsisdn(), event.getChargeAmount());
					chargeAmountTask.execute();
				}
				
			} else {
				log.error("Invalid request:" + body.toString());
			}
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

	private void sendMessage(String msisdn,List<String> messages, String senderAddr, Exchange exchange) {
		if(messages != null && messages.size() > 0) {
			ProducerTemplate template = exchange.getContext().createProducerTemplate();
			template.setDefaultEndpointUri("direct:smppLoadBalence");
			exchange.getIn().setHeader("msisdn", msisdn);
			exchange.getIn().setHeader("senderAddr", senderAddr);
			exchange.getIn().setHeader("message", messages);
			template.send(exchange);
		}
	}

	private void executeWorkflow(SmsParser parser, ExternalNotifcationEvent event) throws WSException {
	/*	NotificationWorkflowService service = NotificationEngineContext.getBean(event.getWsClientId(),
				NotificationWorkflowService.class);*/
		
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

package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.prodcat.tasks.Notification;
import com.ericsson.raso.sef.core.smpp.SmppMessage;

public class NotificationStep extends Step<NotificationStepResult> {
	private static final long serialVersionUID = 6645187522590773212L;
	private static final Logger logger = LoggerFactory.getLogger(NotificationStep.class);

	NotificationStep(String stepCorrelator, Notification executionInputs) {
		super(stepCorrelator, executionInputs);
	}

	@Override
	public NotificationStepResult execute() {
		try {
			logger.debug("Preparing the notification");
			Notification notification = (Notification) getExecutionInputs();

			logger.debug("Creating the SMPP Message");
			SmppMessage message = new SmppMessage();

			String subscriberId = notification.getSubscriberId();
			logger.debug("Destination MSISDN: {}", subscriberId);
			message.setDestinationMsisdn(subscriberId);

			String eventId = notification.getEventId();
			StringBuilder body = new StringBuilder(eventId);
			logger.debug("Notification event ID: {}", eventId);
			if (notification.getMetas() != null) {
				for (Entry<String, Object> meta : notification.getMetas().entrySet()) {
					if (meta.getValue() instanceof CharSequence) {
						body.append(',');
						body.append(meta.getKey());
						body.append(':');
						body.append(meta.getValue());
						logger.debug("Notification Meta: ({}:{})", meta.getKey(), meta.getValue());
					}
				}
			}
			message.setMessageBody(body.toString());

			logger.debug("Sending notification message to ActiveMQ");
			ServiceResolver.getCamelContext().createProducerTemplate().sendBody("activemq:queue:notification", message);

			logger.debug("Sent notification message successfully");
			return new NotificationStepResult(null, true);
		} catch (Exception e) {
			logger.error("Sent notification message failed", e);
			return new NotificationStepResult(new StepExecutionException(e.getMessage(), e), true);
		}

	}

	@Override
	public String toString() {
		return "<NotificationStep executionInputs='" + getExecutionInputs() + "' getResult='" + getResult() + "/>";
	}

}

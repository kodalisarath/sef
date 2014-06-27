package com.ericsson.raso.sef.smart.subscriber.response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class ReadSubscriberProcessor implements Processor {
	private final static Logger logger = LoggerFactory.getLogger(ReadSubscriberProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.debug("Inside process method of ReadSubscriberProcessor");
		Object[] objectArray=(Object[]) exchange.getIn().getBody(Object[].class);
		String correlationId = (String)objectArray[0];
	 	TransactionStatus fault =(TransactionStatus)objectArray[1];
	 	Subscriber subscriber =(Subscriber)objectArray[2];
	 	logger.debug("Inside process method of subscriber object is "+subscriber);
	 	SubscriberResponseHandler subscriberResponsehandler=new SubscriberResponseHandler();
	 	logger.debug("Inside process method of subscriber fault is "+fault +" correlationId is "+correlationId);
	 	subscriberResponsehandler.readSubscriber(correlationId, fault, subscriber);
	}

}

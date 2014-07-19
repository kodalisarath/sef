package com.ericsson.raso.sef.cg.engine.usecase;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsecaseProcessor implements Processor {

	private static Logger logger = LoggerFactory
			.getLogger(UsecaseProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		try {

			String operation = (String) exchange.getIn().getHeader(
					"operationName");
			if(logger.isDebugEnabled())
			logger.debug("Operation Name is " + operation);
			Usecase usecase = Usecase.getUsecaseByOperation(operation);
			if(logger.isDebugEnabled())
			logger.debug("Usecase Name is "
					+ usecase.getProcessor().getClass().getCanonicalName());
			usecase.getProcessor().process(exchange);
		} catch (Exception e) {
			
			logger.error("Exception is UsecaseProcessor",e);
			throw e;
		}
	}
}

package com.ericsson.sef.chargingadapter;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.FetchRequestContextTask;
import com.ericsson.raso.sef.core.ReqContext;
import com.ericsson.raso.sef.core.SmException;


public class CaExceptionHandler implements Processor {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {

		ReqContext requestContext = new FetchRequestContextTask().execute();
		
		Throwable error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
		log.error(error.getMessage(), error);
		
		long resultCode = 500l;
		if (error instanceof SmException) {
			SmException e = (SmException) error;
			long errorCode = e.getStatusCode().getCode();
			ChargingAdapterEdrProcessor.printError(e.getStatusCode());
			resultCode = ResponseCodeUtil.getMappedResultCode(errorCode);
		}
		
		
		requestContext.put(Constants.EXCEPTION, resultCode);
		throw (Exception)error;
	}
}

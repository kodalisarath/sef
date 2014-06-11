package com.ericsson.raso.sef.smart;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SmException;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.wsdl._1.TisException;

public class SmartExceptionHandler implements Processor {
	
	protected static final String DETAILS = "<detail></detail>";

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws TisException {
		TisException exception = null;
		
			Throwable error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
			log.error("Recieved error of Type Throwable "+error.getMessage(), error);
			
			if (error instanceof SmException) {
				SmException e = (SmException) error;
				log.error("error is SmException instance "+e.getStatusCode().toString(), e);
				exception = ExceptionUtil.toTisException(((SmException) error).getStatusCode());
			} else {
				log.error("error is not a SmException instance "+error.getMessage(), error);
				exception = ExceptionUtil.toTisException(ErrorCode.internalServerError);
			}

			throw exception;
			//new SmartEDRProcessor().printError(exception.getFaultInfo().getErrorInfo().get(0));
	}
	

	
}

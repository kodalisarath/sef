package com.ericsson.raso.sef.smart;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.helpers.DOMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.ericsson.raso.sef.core.SmException;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.wsdl._1.TisException;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ErrorInfo;

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

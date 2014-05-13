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
		try {
			Throwable error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
			log.error(error.getMessage(), error);
			
			if (error instanceof SmException) {
				SmException e = (SmException) error;
				log.error(e.getStatusCode().toString(), e);
				exception = ExceptionUtil.toTisException(((SmException) error).getStatusCode());
			} else {
				log.error(error.getMessage(), error);
				exception = ExceptionUtil.toTisException(ErrorCode.internalServerError);
			}

			ErrorInfo errorInfo = exception.getFaultInfo().getErrorInfo().get(0);
			
			JAXBContext context = getJaxbContext();
			QName faultCode = new QName("http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1", errorInfo.getCode());
			SoapFault fault = new SoapFault(errorInfo.getText(), faultCode);
			Element details = DOMUtils.readXml(new StringReader(DETAILS)).getDocumentElement();
			context.createMarshaller().marshal(exception.getFaultInfo(), details);
			fault.setDetail(details);
			exchange.getOut().setBody(fault);
			exchange.getOut().setFault(true);
			//new SmartEDRProcessor().printError(exception.getFaultInfo().getErrorInfo().get(0));
		} catch (Exception e) {
			throw exception;
		}
	}
	
	private static JAXBContext jaxbContext;
	
	private JAXBContext getJaxbContext() throws Exception {
		if(jaxbContext == null) {
			jaxbContext = JAXBContext.newInstance("com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1");
		}
		return jaxbContext;
	}
	
}

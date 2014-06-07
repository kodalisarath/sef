package com.ericsson.raso.sef.smart.usecase;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandRequestData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;

public class UsecaseProcessor implements Processor {

	Logger logger = LoggerFactory.getLogger(UsecaseProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		logger.debug("Entering SMFE frontend request received");
		CommandRequestData commandRequestData = exchange.getIn().getBody(CommandRequestData.class);
		logger.debug("Exchange in UseCaseProcessor: " + exchange);
		boolean isTRansactional = commandRequestData.getCommand().getTransaction()!=null;
		Operation operation = commandRequestData.getCommand().getOperation();
		if(isTRansactional) {
			operation = (Operation) commandRequestData.getCommand().getTransaction().getAssignmentOrOperation().get(0);
		}
		
		Usecase usecase = Usecase.getUsecaseByOperation(operation);
		
		SmartRequest request = usecase.cerateRequest();
		request.setUsecase(usecase);
		request.setTransactional(isTRansactional);
		request.prepareRequest(operation);
		
		exchange.getIn().setBody(request);

		usecase.getRequestProcessor().process(exchange);
		logger.debug("Usecase identified and delegated");
	}

	@Override
	public String toString() {
		return "UsecaseProcessor [logger=" + logger + "]";
	}
	
}

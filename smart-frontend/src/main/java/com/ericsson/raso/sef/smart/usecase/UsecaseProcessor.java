package com.ericsson.raso.sef.smart.usecase;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandRequestData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;

public class UsecaseProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		CommandRequestData commandRequestData = exchange.getIn().getBody(CommandRequestData.class);
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
	}
}

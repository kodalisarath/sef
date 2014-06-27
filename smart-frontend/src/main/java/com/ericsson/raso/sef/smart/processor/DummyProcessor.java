package com.ericsson.raso.sef.smart.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

//TODO: Refactor the class name to NSN Default Success Response
public class DummyProcessor implements Processor {
	
	public static void response(Exchange exchange) {

		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);

		TransactionResult transactionResult = new TransactionResult();

		OperationResult operationResult = new OperationResult();

		transactionResult.getOperationResult().add(operationResult);
		
		result.setTransactionResult(transactionResult);
		String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 
		exchange.getOut().setBody(responseData);
		exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		response(exchange);
	}
	
}

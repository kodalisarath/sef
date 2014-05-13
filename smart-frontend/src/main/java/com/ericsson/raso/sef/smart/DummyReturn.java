package com.ericsson.raso.sef.smart;

import org.apache.camel.Header;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class DummyReturn {
	
	public CommandResponseData response(@Header("operation") String operationName, @Header("modifier") String modifier) {

		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);

		TransactionResult transactionResult = new TransactionResult();

		OperationResult operationResult = new OperationResult();

		transactionResult.getOperationResult().add(operationResult);
		
		result.setTransactionResult(transactionResult);
		
		return responseData;
	}

}

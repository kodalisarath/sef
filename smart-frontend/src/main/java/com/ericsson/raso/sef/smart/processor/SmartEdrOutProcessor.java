package com.ericsson.raso.sef.smart.processor;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class SmartEdrOutProcessor extends SmartEDRProcessor {

	@Override
	public void process(Exchange exchange) throws Exception {
		try {
			if(!log.isInfoEnabled()) return; 
			CommandResponseData response = exchange.getIn().getBody(CommandResponseData.class);
			Printer printer = new Printer();
			printer.edrMap = new LinkedHashMap<String, Object>();
			printer.type = "Response";
			
			TransactionResult transactionResult = response.getCommandResult().getTransactionResult();
			
			if(transactionResult != null && transactionResult.getOperationResult() != null) {
				for (OperationResult operationResult : transactionResult.getOperationResult()) {
					List<Operation> operations = operationResult.getOperation();
					if(operations != null) {
						for (Object object : operations) {
							Operation operation = (Operation) object;
							List<Object> params = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
							printer.edrMap.put(operation.toString(), fromParameters(params));
						}
					}
				}
			}
			
			CommandResult commandResult = response.getCommandResult();
			if(commandResult != null && commandResult.getOperationResult() != null) {
				List<Operation> operations = commandResult.getOperationResult().getOperation();
				if(operations != null) {
					for (Object object : operations) {
						Operation operation = (Operation) object;
						List<Object> params = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
						printer.edrMap.put(operation.toString(), fromParameters(params));
					}
				}
			}
			log.info(printer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

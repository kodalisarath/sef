package com.ericsson.raso.sef.edr.processor;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class SmartEdrOutProcessor extends SmartEDRProcessor {

	@Override
	public void process(Exchange exchange) throws Exception {
		try {
			//log.debug("Inside SmartEdrOutProcessor ..");
			if(!log.isInfoEnabled()) return; 
			CommandResponseData response = exchange.getIn().getBody(CommandResponseData.class);
			Printer printer = new Printer();
			printer.setExchange(exchange);
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
			/*ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore("EDR_PROC_"+exchange.getIn().getHeader("EDR_IDENTIFIER"));
			ISemaphore semaphoreClean = SefCoreServiceResolver
					.getCloudAwareCluster().getSemaphore(
							"CLEAN_PROC_"
									+ exchange.getIn().getHeader(
											"EDR_IDENTIFIER"));

			try {
				semaphore.acquire();
				semaphoreClean.init(0);
				semaphoreClean.acquire();
			} catch (InterruptedException e) {

			}
*/		
			log.info(printer.toString());
			
/*			semaphore.release();
			semaphoreClean.release();
*/			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			SmartEDRProcessor.staticEdrMap.remove(exchange.getOut().getHeader("EDR_IDENTIFIER"));			
		}
	}
}

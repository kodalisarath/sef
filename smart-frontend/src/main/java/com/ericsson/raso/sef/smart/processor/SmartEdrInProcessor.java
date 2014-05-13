package com.ericsson.raso.sef.smart.processor;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandRequestData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Transaction;

public class SmartEdrInProcessor extends SmartEDRProcessor {

	@Override
	public void process(Exchange exchange) throws Exception {
		try {
			if(!log.isInfoEnabled()) return; 
			CommandRequestData request = exchange.getIn().getBody(CommandRequestData.class);
			edrLocal.set(new SmartEdr());
			
			Printer printer = new Printer();
			printer.edrMap = new LinkedHashMap<String, Object>();
			printer.type = "Request";
			
			Transaction transaction = request.getCommand().getTransaction();
			
			if(transaction != null && transaction.getAssignmentOrOperation() != null) {
				List<Object> operations = transaction.getAssignmentOrOperation();
				for (Object object : operations) {
					if(object instanceof Operation) {
						Operation operation = (Operation) object;
						edrLocal.get().useCase = operation.toString();
						List<Object> params = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
						printer.edrMap.putAll(fromParameters(params));
					}
				}
			}
			
			Operation operation = request.getCommand().getOperation();
			if(operation != null) {
				List<Object> params = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
				edrLocal.get().useCase = operation.toString();
				printer.edrMap.putAll(fromParameters(params));
			}
			log.info(printer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

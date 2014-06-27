package com.ericsson.raso.sef.edr.processor;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandRequestData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Transaction;

public class SmartEdrInProcessor extends SmartEDRProcessor {

	// private static Logger logger =
	// LoggerFactory.getLogger(SmartEdrInProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		try {

			// logger.debug("log.isInfoEnabled()  is "+log.isInfoEnabled());
			//log.debug("Inside SmartEdrInProcessor");
			if (!log.isInfoEnabled())
				return;
			CommandRequestData request = exchange.getIn().getBody(
					CommandRequestData.class);

			// edrLocal.set(new SmartEdr());
			SmartEdr smartEdr = new SmartEdr();
			smartEdr.setTransactionId((String)exchange.getIn().getHeader("EDR_IDENTIFIER"));
			Printer printer = new Printer();
			printer.setExchange(exchange);
			printer.edrMap = new LinkedHashMap<String, Object>();
			printer.type = "Request";

			Transaction transaction = request.getCommand().getTransaction();

			if (transaction != null
					&& transaction.getAssignmentOrOperation() != null) {
				List<Object> operations = transaction
						.getAssignmentOrOperation();
				for (Object object : operations) {
					if (object instanceof Operation) {
						Operation operation = (Operation) object;
						smartEdr.setUseCase(operation.toString());
						SmartEDRProcessor.staticEdrMap.put((String) exchange
								.getIn().getHeader("EDR_IDENTIFIER"), smartEdr);
						// edrLocal.get().useCase = operation.toString();
						List<Object> params = operation
								.getParameterList()
								.getParameterOrBooleanParameterOrByteParameter();
						printer.edrMap.putAll(fromParameters(params));
						// log.debug("Params at printerEDRMap 1"+fromParameters(params));
					}
				}
			}

			Operation operation = request.getCommand().getOperation();
			if (operation != null) {
				List<Object> params = operation.getParameterList()
						.getParameterOrBooleanParameterOrByteParameter();
				// edrLocal.get().useCase = operation.toString();
				smartEdr.setUseCase(operation.toString());
				SmartEDRProcessor.staticEdrMap.put((String) exchange.getIn()
						.getHeader("EDR_IDENTIFIER"), smartEdr);

				printer.edrMap.putAll(fromParameters(params));
				// log.debug("Params at printerEDRMap 2" +
				// fromParameters(params));
			}

			/*ISemaphore semaphore = SefCoreServiceResolver
					.getCloudAwareCluster().getSemaphore(
							"EDR_PROC_"
									+ exchange.getIn().getHeader(
											"EDR_IDENTIFIER"));
			try {
				semaphore.init(0);
				semaphore.acquire();
			} catch (InterruptedException e) {

			}*/
			log.info(printer.toString());
			//semaphore.release();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

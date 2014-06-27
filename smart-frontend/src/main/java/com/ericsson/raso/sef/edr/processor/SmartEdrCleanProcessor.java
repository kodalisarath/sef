package com.ericsson.raso.sef.edr.processor;


import org.apache.camel.Exchange;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.hazelcast.core.ISemaphore;

public class SmartEdrCleanProcessor extends SmartEDRProcessor {


	@Override
	public void process(Exchange exchange) throws Exception {
		try {

		/*	ISemaphore semaphoreClean = SefCoreServiceResolver
					.getCloudAwareCluster().getSemaphore(
							"CLEAN_PROC_"
									+ exchange.getIn().getHeader(
											"EDR_IDENTIFIER"));
			try {
				
				semaphoreClean.acquire();
			} catch (InterruptedException e) {

			}*/
			SmartEDRProcessor.staticEdrMap.remove(exchange.getIn().getHeader("EDR_IDENTIFIER"));
			//semaphoreClean.release();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.ericsson.raso.sef.bes.engine.fulfillment.response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.FulfillmentStepResult;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.OrchestrationManager;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class FulfillResponseProcessor implements Processor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Object[] objectArray = exchange.getIn().getBody(Object[].class);
		String correlationId = (String) objectArray[0];
		TransactionStatus status = (TransactionStatus)objectArray[1];
		List<Product> products =(List<Product>)objectArray[2];
		List<Meta> metas = (List<Meta>)objectArray[3];
		
		Set<AtomicProduct> atomicProducts = new HashSet<AtomicProduct>();
		for(Product product: products) 
			atomicProducts.add(TransactionServiceHelper.getApiEntity(product));
		
		//TODO: based on the transaction status set a transaction fault while posting back the step result
		//TODO: fix the impedence of passing the metas from fulfillment to upstream. Fix required in FulfillmentStepResult. Approval pending Sathya
		FulfillmentStepResult result = new FulfillmentStepResult(null, atomicProducts);
		OrchestrationManager.getInstance().promoteFulfillmentExecution(correlationId, result);
		
	}

}

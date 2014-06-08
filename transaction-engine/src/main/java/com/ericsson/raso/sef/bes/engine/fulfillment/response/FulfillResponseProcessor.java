package com.ericsson.raso.sef.bes.engine.fulfillment.response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.FulfillmentStepResult;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.OrchestrationManager;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.StepExecutionException;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class FulfillResponseProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(FulfillResponseProcessor.class);

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Object[] objectArray = exchange.getIn().getBody(Object[].class);
		String correlationId = (String) objectArray[0];
		TransactionStatus status = (TransactionStatus)objectArray[1];
		List<Product> products =(List<Product>)objectArray[2];
		List<Meta> metas = (List<Meta>)objectArray[3];

		logger.debug("Fulfillment response received for: " + correlationId);

		Set<AtomicProduct> atomicProducts = new HashSet<AtomicProduct>();
		FulfillmentStepResult result=null;
		if(status != null  && status.getCode() > 0){
			ResponseCode resonseCode = new ResponseCode(status.getCode(),status.getDescription());
			StepExecutionException stepExecutionException = new StepExecutionException(status.getComponent(),resonseCode);
			result = new FulfillmentStepResult(stepExecutionException, null);
		}else{
			if(products != null) {
				for(Product product: products) {
					logger.debug("Product Quota defined: " +  product.getQuotaDefined() + " Quota consumed: " + product.getQuotaConsumed() + " Metas: " + product.getMetas());
					atomicProducts.add(TransactionServiceHelper.getApiEntity(product));
					logger.debug("still in loop...");
				}
			}
			//TODO: based on the transaction status set a transaction fault while posting back the step result
			//TODO: fix the impedence of passing the metas from fulfillment to upstream. Fix required in FulfillmentStepResult. Approval pending Sathya
			logger.debug("out of loop, creating response...");
			result = new FulfillmentStepResult(null, atomicProducts);
			logger.debug("here is the response: " + result);
		}
		logger.debug("Fulfillment completed. Engaging orchestration manager to update the response for " +  correlationId);
		OrchestrationManager.getInstance().promoteFulfillmentExecution(correlationId, result);

	}

}

package com.ericsson.raso.sef.smart.subscription.response;

import java.util.List;

import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class PurchaseResponseProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseResponseProcessor.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {

		logger.debug("Purchase Response arrived!!!");
		Object[] objectArray=(Object[]) exchange.getIn().getBody(Object[].class);
		String correlationId = (String)objectArray[0];
	 	TransactionStatus fault =(TransactionStatus)objectArray[1];
	 	String subscriptionId = (String) objectArray[2]; 
	 	List<Product> products =(List<Product>) objectArray[3];
	 	List<Meta> metas =(List<Meta>) objectArray[4];
		
	 	SubscriptionResponseHandler responseHandler = new SubscriptionResponseHandler();
	 	logger.debug("Notifying handler with correlationID: " + correlationId + " New subscriptionid: " + subscriptionId);
	 	if(products!=null) {
	 		for(Product product: products) {
	 			logger.debug("Balances Defined: " + product.getQuotaDefined() + "consumed: " + product.getQuotaConsumed());
	 		}
	 	}
	 	responseHandler.purchase(correlationId, fault, subscriptionId, products, metas);
	}

}

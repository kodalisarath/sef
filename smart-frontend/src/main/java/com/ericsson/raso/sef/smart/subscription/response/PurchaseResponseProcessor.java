package com.ericsson.raso.sef.smart.subscription.response;

import java.util.List;

import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class PurchaseResponseProcessor implements Processor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {

		Object[] objectArray=(Object[]) exchange.getIn().getBody(Object.class);
		String correlationId = (String)objectArray[0];
	 	Holder<TransactionStatus> fault =(Holder<TransactionStatus>)objectArray[1];
	 	Holder<String> subscriptionId = (Holder<String>) objectArray[2]; 
	 	Holder<List<Product>> products =(Holder<List<Product>>) objectArray[3];
	 	Holder<List<Meta>> metas =(Holder<List<Meta>>) objectArray[4];
		
	 	SubscriptionResponseHandler responseHandler = new SubscriptionResponseHandler();
	 	responseHandler.purchase(correlationId, fault.value, subscriptionId.value, products.value, metas.value);
	}

}

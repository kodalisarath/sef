package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.Map;

import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class PurchaseProcessor implements Processor{

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		Object[] objectArray = (Object[]) arg0.getIn().getBody(Object.class);
		Holder<String> requestId = (Holder<String>)objectArray[0];
		Holder<String> offerId = (Holder<String>)objectArray[1];
		Holder<String> subscriberId=(Holder<String>)objectArray[2];
		Holder<Map> metas=(Holder<Map>)objectArray[3];
		Holder<Boolean> override=(Holder<Boolean>)objectArray[4];
		TransactionManager transactionManager=ServiceResolver.getTransactionManager();
		transactionManager.purchase(requestId.value, offerId.value, subscriberId.value, override.value, metas.value);
		
	}

}

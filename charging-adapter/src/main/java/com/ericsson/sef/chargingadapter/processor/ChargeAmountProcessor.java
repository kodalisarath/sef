package com.ericsson.sef.chargingadapter.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.sef.chargingadapter.ChargingAdapterEdrProcessor;
import com.ericsson.sef.chargingadapter.task.DirectDebitTask;

public class ChargeAmountProcessor implements Processor {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public void process(Exchange exchange) throws SmException, PersistenceError {
		log.debug("........Inside ChargeAmountProcessor....... ");
		String endUserIdentifier = (String) exchange.getIn().getHeader("endUserIdentifier");
		ChargingInformation charge = (ChargingInformation) exchange.getIn().getHeader("charge");
		String referenceCode =  (String) exchange.getIn().getHeader("referenceCode"); 
		log.debug("endUserIdentifier: "+endUserIdentifier);
		ChargingAdapterEdrProcessor.requestIn(endUserIdentifier, charge, referenceCode);
		DirectDebitTask directDebitTask = new DirectDebitTask(referenceCode, endUserIdentifier,charge);
		directDebitTask.execute();
		ChargingAdapterEdrProcessor.responseOut();
	}
}

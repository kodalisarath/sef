package com.ericsson.raso.sef.cg.engine.cdr;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.DateUtil;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.diameter.DiameterUtil;

public class CdrProcessor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Handler
	public void requestIn(Exchange exchange) {
		
		try {
			ChargingInfo request = (ChargingInfo) exchange.getIn().getBody();
			Printer printer = new Printer();
			printer.type = "Request";
			String transactionId = request.getUniqueMessageId();
			if(transactionId == null)
				transactionId = UniqueIdGenerator.generateId();
			exchange.getIn().setHeader("CDR_TransactionId", transactionId);
			printer.transactionId = transactionId;
			printer.edrMap = new LinkedHashMap<String, Object>();
			printer.edrMap.putAll(DiameterUtil.toMap(request.getAvpList()));
			log.info(printer.toString());
		} catch (Exception e) {
			
			log.error("Generic Exception Captured at requestIn ", e);
		
		}
	}

	@Handler
	public void responseOut(Exchange exchange) {
		
		try {
			ChargingInfo response = (ChargingInfo) exchange.getIn().getBody();
			Printer printer = new Printer();
			printer.type = "Response";
			printer.transactionId = (String)exchange.getIn().getHeader("CDR_TransactionId");
			printer.edrMap = new LinkedHashMap<String, Object>();
			printer.edrMap.putAll(DiameterUtil.toMap(response.getAvpList()));
			log.info(printer.toString());
		} catch (Exception e) {
			log.error("Generic Exception Captured at responseOut ", e);
		}
	}

	private static class Printer {
		String type;
		String transactionId;
		Map<String, Object> edrMap = new LinkedHashMap<String, Object>();

		@Override
		public String toString() {
			return "Type=" + type + ",TransactionId=" + transactionId + ",Timestamp=" + DateUtil.toEdrFormat(new Date()) + ",Component=charging-gateway," 
					+ (edrMap.size() != 0 ? edrMap.toString() : "") ;
		}
	}
}

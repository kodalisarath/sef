package com.ericsson.raso.sef.cg.engine;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.charginggateway.diameter.ChargingInfo;
import com.ericsson.raso.sef.charginggateway.diameter.DiameterUtil;
import com.ericsson.raso.sef.core.DateUtil;

public class CdrProcessor {

	private Logger log = LoggerFactory.getLogger("CG");

	@Handler
	public void requestIn(Exchange exchange) {
		
		try {
			ChargingInfo request = (ChargingInfo) exchange.getIn().getBody();
			Printer printer = new Printer();
			printer.type = "Request";
			printer.transactionId = request.getUniqueMessageId();
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
			printer.transactionId = response.getUniqueMessageId();
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

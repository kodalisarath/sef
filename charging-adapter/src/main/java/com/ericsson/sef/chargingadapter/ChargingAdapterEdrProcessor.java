package com.ericsson.sef.chargingadapter;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.StatusCode;

public class ChargingAdapterEdrProcessor {

	private static Logger log = LoggerFactory.getLogger("CA");

	private static final ThreadLocal<ChargingAdapterEdr> edrLocal = new ThreadLocal<ChargingAdapterEdr>() {};

	public static class ChargingAdapterEdr {
		public ChargingAdapterEdr(String transactionId) {
			this.transactionId = transactionId;
		}

		private String transactionId;
		private String useCase;
	}

	public static void requestIn(String endUserIdentifier,ChargingInformation charge,String referenceCode) {

		if (log.isInfoEnabled()) {
			edrLocal.set(new ChargingAdapterEdr(referenceCode));

			try {
				Printer printer = new Printer();
				printer.edrMap = new LinkedHashMap<String, Object>();
				printer.type = "Request";
				edrLocal.get().useCase = "ChargeAmount";
				printer.edrMap.put("endUserIdentifier", endUserIdentifier);

				Map<String, Object> chargeMap = new HashMap<String, Object>();

				if (charge != null) {
					chargeMap.put("description", charge.getDescription());
					chargeMap.put("amount", charge.getAmount());
					chargeMap.put("code", charge.getCode());
					chargeMap.put("currency", charge.getCurrency());
					printer.edrMap.put("ChargingInformation", chargeMap);
				}
				log.info(printer.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void responseOut(){
		if (log.isInfoEnabled()) {
			try {
				Printer printer = new Printer();
				printer.edrMap = new LinkedHashMap<String, Object>();
				printer.type = "Response";
				printer.edrMap = new LinkedHashMap<String, Object>();
				printer.edrMap.put("Response Code ", 0);
				log.info(printer.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void printError(StatusCode errorInfo) {
		if (log.isInfoEnabled()) {
			Printer printer = new Printer();
			printer.type = "Response";
			printer.edrMap = new LinkedHashMap<String, Object>();
			printer.edrMap.put("ErrorCode", errorInfo.getCode());
			printer.edrMap.put("ErrorMessage", errorInfo.getMessage());
			log.info(printer.toString());
		}
	}

	private static class Printer {
		String type;
		Map<String, Object> edrMap = new LinkedHashMap<String, Object>();

		@Override
		public String toString() {
			return "Type=" + type + ",TransactionId=" + edrLocal.get().transactionId +  ",UseCase=" + edrLocal.get().useCase + ",Timestamp="
					+ new Date().toString() + ",Component=charging-adapter,"
					+ (edrMap.size() != 0 ? edrMap.toString() : "");
		}
	}
}
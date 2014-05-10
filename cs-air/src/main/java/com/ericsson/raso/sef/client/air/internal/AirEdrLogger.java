package com.ericsson.raso.sef.client.air.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.DateUtil;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.StatusCode;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;


@SuppressWarnings({"unchecked", "rawtypes"})
public class AirEdrLogger {

	//TODO: update logger based on framework logger service
	private static Logger log = LoggerFactory.getLogger("CSAIR");

	private static final ThreadLocal<AirEdr> edrLocal = new ThreadLocal<AirEdr>() {
	};

	public static class AirEdr {
		public AirEdr() {
			transactionId = RequestContextLocalStore.get().getRequestId();
		}

		private String transactionId;
		private String useCase;
	}

	public static void requestIn(String methodName, Object[] request) {
		if(!log.isInfoEnabled()) return;
	
		edrLocal.set(new AirEdr());

		try {
			Printer printer = new Printer();
			printer.edrMap = new LinkedHashMap<Object, Object>();
			printer.type = "Request";
			edrLocal.get().useCase = methodName;
			printer.edrMap.putAll(printEDR((Map<Object, Object>) request[0]));
			log.info(printer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void responseError(String methodName, XmlRpcException exception) {
		if(!log.isInfoEnabled()) return;
		try {
			Printer printer = new Printer();
			printer.edrMap = new LinkedHashMap<Object, Object>();
			printer.type = "Response";
			printer.edrMap.put("Error", exception.getMessage());
			log.info(printer.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void responseOut(String methodName, Object response) {
		if(!log.isInfoEnabled()) return;
		try {
			Printer printer = new Printer();
			printer.edrMap = new LinkedHashMap<Object, Object>();
			printer.type = "Response";
			printer.edrMap.putAll(printEDR((Map<Object, Object>) response));
			log.info(printer.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private static Map<Object, Object> printEDR(Map<Object, Object> map) {
		Map<Object, Object> finalEdrMap = new HashMap<Object, Object>();
		for (Object key : map.keySet()) {

			Object val = map.get(key);
			if (val instanceof Map) {
				finalEdrMap.put(key, printEDR((Map<Object, Object>) val));
			} else if (val instanceof Map[]) {
				Map[] mapVals = (Map[]) val;
				for (Map valObjects : mapVals) {
					finalEdrMap.put(key, printEDR((Map<Object, Object>) valObjects));
				}
			} else if (val instanceof List<?>) {
				List<Object> listval = (List<Object>) val;
				for (Object obj : listval) {
					finalEdrMap.put(key, printEDR((Map<Object, Object>) obj));
				}
			} else if (val instanceof Object[]) {
				Object[] arrval = (Object[]) val;
				List<Object> list = new ArrayList<Object>();
				for (Object object : arrval) {
					Object a = object;
					if(a instanceof Map) {
						a = printEDR((Map)a);
					}
					if(object instanceof Date) {
						a = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(object);
					}
					list.add(a);
				}
				finalEdrMap.put(key, list);
			} else if (val instanceof Date) {
				finalEdrMap.put(key, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(val));
			}
			else {
				finalEdrMap.put(key, val);
			}
		}
		return finalEdrMap;
	}

	public static void printError(StatusCode errorInfo) {
		Printer printer = new Printer();
		printer.type = "Response";
		printer.edrMap = new LinkedHashMap<Object, Object>();
		printer.edrMap.put("ErrorCode", errorInfo.getCode());
		printer.edrMap.put("ErrorMessage", errorInfo.getMessage());
		log.info(printer.toString());
	}

	private static class Printer {
		String type;
		Map<Object, Object> edrMap = new LinkedHashMap<Object, Object>();

		@Override
		public String toString() {
			return "Type=" + type + ",TransactionId=" + edrLocal.get().transactionId + ",UseCase=" + edrLocal.get().useCase + ",Timestamp="
					+ DateUtil.toEdrFormat(new Date()) + ",Component=cs-air," + (edrMap.size() != 0 ? edrMap.toString() : "");
		}
	}

}

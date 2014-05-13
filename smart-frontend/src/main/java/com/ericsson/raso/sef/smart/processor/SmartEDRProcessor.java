package com.ericsson.raso.sef.smart.processor;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.DateUtil;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.BooleanElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.BooleanParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ByteElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ByteParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DoubleElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DoubleParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ErrorInfo;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.FloatElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.FloatParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ShortElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ShortParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StructElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StructParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.SymbolicElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.SymbolicParameter;

public abstract class SmartEDRProcessor implements Processor {

	public static Logger log = LoggerFactory.getLogger("smartFE");
	
	public static final ThreadLocal<SmartEdr> edrLocal = new ThreadLocal<SmartEdr>() {};
    
    public static class SmartEdr {
    	public SmartEdr() {
    		transactionId = RequestContextLocalStore.get().getRequestId();
    	}
    	
    	String transactionId;
    	String useCase;
    }

    protected Map<String, Object> fromParameters(List<Object> params) {
		Map<String, Object> edrMap = new LinkedHashMap<String, Object>();
		if(params == null) return edrMap;
		for (Object param : params) {
			if (param instanceof StringParameter) {
				StringParameter strParam = (StringParameter) param;
				if (strParam.getValue() != null) {
					edrMap.put(strParam.getName(), strParam.getValue().trim());
				}
			} else if (param instanceof BooleanParameter) {
				BooleanParameter boolParam = (BooleanParameter) param;
				edrMap.put(boolParam.getName(), "" + boolParam.isValue());
				edrMap.put(boolParam.getName(), "" + boolParam.isValue());
			} else if (param instanceof IntParameter) {
				IntParameter intParam = (IntParameter) param;
				edrMap.put(intParam.getName(), "" + intParam.getValue());
			} else if (param instanceof ShortParameter) {
				ShortParameter shortParam = (ShortParameter) param;
				edrMap.put(shortParam.getName(), "" + shortParam.getValue());
			} else if (param instanceof LongParameter) {
				LongParameter longParam = (LongParameter) param;
				edrMap.put(longParam.getName(), "" + longParam.getValue());
			} else if (param instanceof ByteParameter) {
				ByteParameter byteParam = (ByteParameter) param;
				edrMap.put(byteParam.getName(), "" + byteParam.getValue());
			} else if (param instanceof DateParameter) {
				DateParameter dateParam = (DateParameter) param;
				if (dateParam.getValue() != null) {
					edrMap.put(dateParam.getName(), dateParam.getValue().toString());
				}
			} else if (param instanceof DateTimeParameter) {
				DateTimeParameter dateTimeParam = (DateTimeParameter) param;
				if (dateTimeParam.getValue() != null) {
					edrMap.put(dateTimeParam.getName(), dateTimeParam.getValue().toString());
				}
			} else if (param instanceof DoubleParameter) {
				DoubleParameter doubleParam = (DoubleParameter) param;
				edrMap.put(doubleParam.getName(), "" + doubleParam.getValue());
			} else if (param instanceof FloatParameter) {
				FloatParameter floatParam = (FloatParameter) param;
				edrMap.put(floatParam.getName(), "" + floatParam.getValue());
			} else if (param instanceof EnumerationValueParameter) {
				EnumerationValueParameter enumParam = (EnumerationValueParameter) param;
				if (enumParam.getValue() != null) {
					edrMap.put(enumParam.getName(), enumParam.getValue().trim());
				}
			} else if (param instanceof SymbolicParameter) {
				SymbolicParameter symParam = (SymbolicParameter) param;
				if (symParam.getValue() != null) {
					edrMap.put(symParam.getName(), symParam.getValue().trim());
				}
			} else if (param instanceof ListParameter) {
				ListParameter listParam = (ListParameter) param;
				List<Object> paramsList = listParam.getElementOrBooleanElementOrByteElement();
				if(listParam.getName().equalsIgnoreCase("RatingInput")) {
					edrMap.put(listParam.getName(), fromRatinInput(paramsList));
				} else {
					edrMap.put(listParam.getName(), fromElements(paramsList));
				}
			} else if (param instanceof StructParameter) {
				StructParameter structParam = (StructParameter) param;
				List<Object> paramsStruct = structParam.getParameterOrBooleanParameterOrByteParameter();
				edrMap.put(structParam.getName(), fromParameters(paramsStruct));
			}
		}
		return edrMap;
	}
	
	protected Map<String, Object> fromRatinInput(List<Object> params) {
		Map<String, Object> edrMap = new LinkedHashMap<String, Object>();
		if(params == null) return edrMap;
		int i = 0;
		for (Object param : params) {
			if (param instanceof StringElement) {
				StringElement strParam = (StringElement) param;
				if (strParam.getValue() != null) {
					edrMap.put("RatingInput_" + String.valueOf(i++), strParam.getValue().trim());
				}
			} 
		}
		
		return edrMap;
	}
	
	
	public static void printError(ErrorInfo errorInfo) {
		if(!log.isInfoEnabled()) return;
		Printer printer = new Printer();
		printer.type = "Response";
		printer.edrMap = new LinkedHashMap<String, Object>();
		printer.edrMap.put("ErrorCode", errorInfo.getCode());
		printer.edrMap.put("ErrorMessage",errorInfo.getText());
		log.info(printer.toString());
	}

	protected Map<String, Object> fromElements(List<Object> params) {
		Map<String, Object> edrMap = new LinkedHashMap<String, Object>();
		if(params == null) return edrMap;
		int i = 0;
		for (Object param : params) {
			if (param instanceof StringElement) {
				StringElement strParam = (StringElement) param;
				if (strParam.getValue() != null) {
					edrMap.put(String.valueOf(i++), strParam.getValue().trim());
				}
			} else if (param instanceof BooleanElement) {
				BooleanElement boolParam = (BooleanElement) param;
				edrMap.put(String.valueOf(i++), "" + boolParam.isValue());
			} else if (param instanceof IntElement) {
				IntElement intParam = (IntElement) param;
				edrMap.put(String.valueOf(i++), "" + intParam.getValue());
			} else if (param instanceof ShortElement) {
				ShortElement shortParam = (ShortElement) param;
				edrMap.put(String.valueOf(i++), "" + shortParam.getValue());
			} else if (param instanceof LongElement) {
				LongElement longParam = (LongElement) param;
				edrMap.put(String.valueOf(i++), "" + longParam.getValue());
			} else if (param instanceof ByteElement) {
				ByteElement byteParam = (ByteElement) param;
				edrMap.put(String.valueOf(i++), "" + byteParam.getValue());
			} else if (param instanceof DateElement) {
				DateElement dateParam = (DateElement) param;
				if (dateParam.getValue() != null) {
					edrMap.put(String.valueOf(i++), dateParam.getValue().toString());
				}
			} else if (param instanceof DateTimeElement) {
				DateTimeElement dateTimeParam = (DateTimeElement) param;
				if (dateTimeParam.getValue() != null) {
					edrMap.put(String.valueOf(i++), dateTimeParam.getValue().toString());
				}
			} else if (param instanceof DoubleElement) {
				DoubleElement doubleParam = (DoubleElement) param;
				edrMap.put(String.valueOf(i++), "" + doubleParam.getValue());
			} else if (param instanceof FloatElement) {
				FloatElement floatParam = (FloatElement) param;
				edrMap.put(String.valueOf(i++), "" + floatParam.getValue());
			} else if (param instanceof EnumerationValueElement) {
				EnumerationValueElement enumParam = (EnumerationValueElement) param;
				if (enumParam.getValue() != null) {
					edrMap.put(String.valueOf(i++), enumParam.getValue());
				}
			} else if (param instanceof SymbolicElement) {
				SymbolicElement symParam = (SymbolicElement) param;
				if (symParam.getValue() != null) {
					edrMap.put(String.valueOf(i++), symParam.getValue().trim());
				}
			} else if (param instanceof ListElement) {
				ListElement listParam = (ListElement) param;
				List<Object> paramsList = listParam.getElementOrBooleanElementOrByteElement();
				edrMap.put(String.valueOf(i++), fromElements(paramsList));
			} else if (param instanceof StructElement) {
				StructElement structParam = (StructElement) param;
				List<Object> paramsStruct = structParam.getParameterOrBooleanParameterOrByteParameter();
				edrMap.put(String.valueOf(i++), fromParameters(paramsStruct));
			}
		}
		return edrMap;
	}

	public static class Printer {
		String type;
		Map<String, Object> edrMap = new LinkedHashMap<String, Object>();

		@Override
		public String toString() {
			return "Type=" + type + ",TransactionId=" + edrLocal.get().transactionId +  ",UseCase=" + edrLocal.get().useCase + ",Timestamp="
					+ DateUtil.toEdrFormat(new Date()) + ",Component=smart-frontend,"
					+ (edrMap.size() != 0 ? edrMap.toString() : "");
		}
	}
}

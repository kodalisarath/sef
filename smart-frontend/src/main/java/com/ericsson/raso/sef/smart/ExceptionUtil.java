package com.ericsson.raso.sef.smart;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.StatusCode;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.wsdl._1.TisException;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ErrorInfo;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.FaultMessage;

public class ExceptionUtil {
	

	private static final Logger logger = LoggerFactory
			.getLogger(ExceptionUtil.class);

	private static Map<String, ErrorCode> allCodes;
	
	public static StatusCode get(String code) {
		if(allCodes == null) {
			allCodes = new HashMap<String, ErrorCode>();
			ErrorCode[] codes = ErrorCode.values();
			for (ErrorCode errorCode : codes) {
				allCodes.put(String.valueOf(errorCode.getCode()), errorCode);
			}
		}
		StatusCode statusCode = allCodes.get(code);
		return statusCode;
	}
	
	public static SmException toSmException(com.ericsson.raso.sef.core.StatusCode statusCode) {
		return new SmException("", statusCode);
	}
	
	public static TisException toTisException(StatusCode code) {
		int errorCode = ErrorCode.internalServerError.getCode();
		String errMessage = ErrorCode.internalServerError.getMessage();
		
		if(code != null && code.getCode() == -111) {
			try {
				String[] strs = code.getMessage().split("#");
				errorCode = Integer.valueOf(strs[0]);
				errMessage = strs[1];
			} catch (Exception e) {
				logger.error("Manila Error:  Exception caught at toTisException",e);
			}
			
		} else {
			String frontenCode = SefCoreServiceResolver.getConfigService().getValue("smart-frontend-responsemap", ""+code.getCode());
			code = get(frontenCode);
			
			if(code == null) {
				code = ErrorCode.internalServerError;
			}
			
			errorCode = code.getCode();
			errMessage = code.getMessage();
		}
		
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setCode(String.valueOf(errorCode));
		errorInfo.setText(errMessage);
		FaultMessage message = new FaultMessage();
		message.getErrorInfo().add(errorInfo);
		TisException exception = new TisException(errMessage, message);
		return exception;
	}
}

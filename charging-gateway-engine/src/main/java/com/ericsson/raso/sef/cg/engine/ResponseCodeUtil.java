package com.ericsson.raso.sef.cg.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class ResponseCodeUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ResponseCodeUtil.class); 
	public static Long getMappedResultCode(Long backendCode) {
		//IConfig config = CgEngineContext.getConfig();
		String code =null;// TBD  Mapping to be done
		//String code = config.getFrontendCode(CgEngineContext.getComponentName(), String.valueOf(backendCode));
		logger.debug("ResponseCodeUtil getMappedResultCode backendCode errorCode is "+backendCode);
		code =SefCoreServiceResolver.getConfigService().getValue("diameterEndpoint", backendCode+"");
		logger.debug("ResponseCodeUtil getMappedResultCode code is "+code);
		
		if(code == null) {
			return  Long.valueOf(ResponseCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
		}
	
		return Long.valueOf(code);
	}
	
	public static Long getMappedExperimentalResultCode(Long backendCode, int backendExtensionCode) {
		//IConfig config = CgEngineContext.getConfig();
		String code =null;// TBD  Mapping to be done
		//String code = config.getFrontendCode(CgEngineContext.getComponentName(), String.valueOf(backendCode) + '-' + String.valueOf(BackendExtensionCode));
		
		logger.debug("ResponseCodeUtil getMappedExperimentalResultCode backendCode errorCode is "+backendCode +" backendExtensionCode is "+backendExtensionCode); 
		code =SefCoreServiceResolver.getConfigService().getValue("diameterEndpoint", backendCode+"-"+backendExtensionCode);
		logger.debug("ResponseCodeUtil getMappedExperimentalResultCode code is "+code);
		if(code == null) {
			return  null;
		}
	
		
		return Long.valueOf(code);
	}
}

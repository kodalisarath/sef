package com.ericsson.sef.chargingadapter;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;

public class ResponseCodeUtil {
	
	public static Long getMappedResultCode(Long backendCode) {
		//IConfig config = CaContext.getConfig();
		IConfig config = SefCoreServiceResolver.getConfigService();
		String code = config.getValue("scapClient", String.valueOf(backendCode));
		if(code == null) {
			return  Long.valueOf(ResponseCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
		}
		return Long.valueOf(code);
	}
	
	public static Long getMappedExperimentalResultCode(Long backendCode, int BackendExtensionCode) {
		IConfig config = SefCoreServiceResolver.getConfigService();
		String code = config.getValue("scapClient", String.valueOf(backendCode) + '-' + String.valueOf(BackendExtensionCode));
		if(code == null) {
			return  null;
		}
		return Long.valueOf(code);
	}
}

package com.ericsson.raso.sef.client.air.request;

import java.util.HashMap;
import java.util.Map;

public abstract class NativeAirRequest {
	
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	
	public Map<String, Object> toNative() {
		return paramMap;
	}
	
	protected void addParam(String key, Object value) {
		paramMap.put(key, value);
	}

}

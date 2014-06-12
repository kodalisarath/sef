package com.ericsson.raso.sef.client.air.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class NativeAirRequest implements Serializable {
	private static final long serialVersionUID = -6218804399266839105L;

	private Map<String, Object> paramMap = new HashMap<String, Object>();
	
	public Map<String, Object> toNative() {
		return paramMap;
	}
	
	protected void addParam(String key, Object value) {
		paramMap.put(key, value);
	}

}

package com.ericsson.raso.sef.client.air.response;

import java.io.Serializable;
import java.util.Map;

public abstract class NativeAirResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	protected final Map<String, Object> paramMap;

	public NativeAirResponse(final Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getParam(String key, Class<T> type) {
		return (T) paramMap.get(key);
	}

}

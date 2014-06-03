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
		Object returned = paramMap.get(key);
		if (returned != null)
			return (T) returned;
		else
			return null;
	}

}

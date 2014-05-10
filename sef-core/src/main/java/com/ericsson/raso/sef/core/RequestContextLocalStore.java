package com.ericsson.raso.sef.core;

public class RequestContextLocalStore {
	
	private static final ThreadLocal<RequestContext> reqCtxLocal = new ThreadLocal<RequestContext>();
	
	public static void put(RequestContext ctx) {
		reqCtxLocal.set(ctx);
	}
	
	public static RequestContext get() {
		return reqCtxLocal.get();
	}

}

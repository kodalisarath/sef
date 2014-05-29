package com.ericsson.raso.sef.core;

public class RequestContextLocalStore {
	
	private static final ThreadLocal<RequestContext> reqCtxLocal = new ThreadLocal<RequestContext>();
	
	public static void put(RequestContext ctx) {
		reqCtxLocal.set(ctx);
	}
	
	public static RequestContext get() {
		if(reqCtxLocal.get() == null){
			RequestContext ctx = new RequestContext();
			reqCtxLocal.set(ctx);
		}
		return reqCtxLocal.get();
	}

}

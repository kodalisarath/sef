package com.ericsson.raso.sef.core.jaxws;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;

import org.apache.cxf.jaxws.JaxWsClientProxy;

import com.ericsson.raso.sef.core.SmException;


public class SefJaxWsClientProxy implements InvocationHandler {

	private JaxWsClientProxy target;
	
	public SefJaxWsClientProxy(JaxWsClientProxy target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			return target.invoke(proxy, method, args);
		} catch (SocketTimeoutException e) {
			throw new SmException(SmException.socketError);
		} finally {
		}
	}
	
}

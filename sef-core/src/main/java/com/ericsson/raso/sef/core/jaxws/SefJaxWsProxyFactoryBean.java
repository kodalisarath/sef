package com.ericsson.raso.sef.core.jaxws;

import java.lang.reflect.Proxy;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.ConnectionType;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.FactoryBean;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;


public class SefJaxWsProxyFactoryBean<T> implements FactoryBean<T> {
	
	private String endpointId;
	private Class<T> serviceType;
	
	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}
	
	public void setServiceType(Class<T> serviceType) {
		this.serviceType = serviceType;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public T getObject() throws Exception {
		JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
		IConfig config = SefCoreServiceResolver.getConfigService();
//		String endpointAddress = config.getValue(endpointId, "address");

		jaxWsProxyFactoryBean.setAddress(this.endpointId);
		jaxWsProxyFactoryBean.setServiceClass(serviceType);

		jaxWsProxyFactoryBean.create();

		String connectionTO = config.getValue(endpointId, "connectionTimeout");
		String receiveTO = config.getValue(endpointId, "receiveTimeout");
		String connectionType = config.getValue(endpointId, "connectionType");

		long connectionTimeout = 60000L;
		long receiveTimeout = 120000L;

		if (connectionTO != null) {
			connectionTimeout = Long.parseLong(connectionTO);
		}

		if (receiveTO != null) {
			receiveTimeout = Long.parseLong(receiveTO);
		}

		HTTPClientPolicy clientPolicy = new HTTPClientPolicy();
		clientPolicy.setConnectionTimeout(connectionTimeout);
		clientPolicy.setReceiveTimeout(receiveTimeout);
		clientPolicy.setConnection(ConnectionType.valueOf(connectionType));

		HTTPConduit httpConduit = (HTTPConduit) jaxWsProxyFactoryBean.jaxWsClientProxy
				.getClient().getConduit();
		httpConduit.setClient(clientPolicy);

		SefJaxWsClientProxy clientProxy = new SefJaxWsClientProxy(
				jaxWsProxyFactoryBean.jaxWsClientProxy);
		Object proxy = Proxy.newProxyInstance(jaxWsProxyFactoryBean
				.getServiceClass().getClassLoader(),
				new Class[] { serviceType }, clientProxy);

		return (T) proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return serviceType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	private static class JaxWsProxyFactoryBean extends org.apache.cxf.jaxws.JaxWsProxyFactoryBean {
		
		JaxWsClientProxy jaxWsClientProxy = null;
		
		@Override
		protected ClientProxy clientClientProxy(Client c) {
			jaxWsClientProxy = (JaxWsClientProxy) super.clientClientProxy(c);
			return jaxWsClientProxy;
		}
		
	}
}

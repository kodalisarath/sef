package com.ericsson.raso.sef.auth.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;

import org.apache.cxf.common.security.SimpleGroup;
import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.cxf.ws.security.wss4j.AbstractUsernameTokenAuthenticatingInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class WssAuthInterceptor extends AbstractUsernameTokenAuthenticatingInterceptor implements CallbackHandler {
	private static final Logger logger = LoggerFactory.getLogger(WssAuthInterceptor.class);

	//TODO: temporary hack to get SMART authenticated.. must connect to auth framework later...
	private static final Set<QName> HEADERS = new HashSet<QName>();
	static {
		HEADERS.add(new QName(WSConstants.WSSE_NS, "Security"));
		HEADERS.add(new QName(WSConstants.WSSE11_NS, "Security"));
		HEADERS.add(new QName(WSConstants.WSU_NS, "UsernameToken"));

	}

	public WssAuthInterceptor() {
		logger.debug("WSS4J & CXF hooking the interceptor in...");
		this.getProperties().put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		// Password type : plain text
		this.getProperties().put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		// for hashed password use:
		//properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
		// Callback used to retrieve password for given user.
		this.getProperties().put(WSHandlerConstants.PW_CALLBACK_CLASS, WssAuthInterceptor.class.getName());
	}

	public WssAuthInterceptor(Map<String, Object> map) {
		this();
	}

	protected Subject createSubject(String name, String password, boolean isDigest, String nonce, String created) {
		Subject subject = new Subject();

		// delegate to the external security system if possible
		logger.debug("Delegating to me for authentication & authorization...");
		

		// authenticate the user somehow
		String storePassword = SefCoreServiceResolver.getConfigService().getValue("SMART_auth", name);
		if (password == null) {
			logger.error("Username (" + name + ") not found authStore!!");
			return null;
		}
		if (!password.equals(storePassword)) {
			logger.error("Username (" + name + ") did not authenticate with the authStore!!");
			return null;
		}

		logger.debug("authenticated....");
		subject.getPrincipals().add(new SimplePrincipal(name));

		// add roles this user is in
		String roleName = "smartClient";
		subject.getPrincipals().add(new SimpleGroup(roleName, name));
		logger.debug("authorized...");
		return subject;
	}
	
	@Override
	public Set<QName> getUnderstoodHeaders() {
		logger.debug("negotiating understood headers...");
		return HEADERS;
	}
	
	

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		logger.debug("Intercept hooked for Password Callback...");

		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		String storePassword = SefCoreServiceResolver.getConfigService().getValue("SMART_auth", pc.getIdentifier());
		if (storePassword == null) {
			logger.error("Username (" + pc.getIdentifier() + ") not found authStore!!");
			throw new IOException("Username (" + pc.getIdentifier() + ") not found authStore!!");
		}
		if (!storePassword.equals(pc.getPassword())) {
			logger.error("Username (" + pc.getIdentifier() + ") did not authenticate with the authStore!!");
			throw new IOException("Username (" + pc.getIdentifier() + ") did not authenticate with the authStore!!");
		}

		logger.debug("Intercept successfully authenticated Username (" + pc.getIdentifier() + ")...");


	}



}

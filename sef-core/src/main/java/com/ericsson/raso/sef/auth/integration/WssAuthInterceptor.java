package com.ericsson.raso.sef.auth.integration;

import java.util.HashMap;

import javax.security.auth.Subject;

import org.apache.cxf.common.security.SimpleGroup;
import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.cxf.ws.security.wss4j.AbstractUsernameTokenAuthenticatingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WssAuthInterceptor extends AbstractUsernameTokenAuthenticatingInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(WssAuthInterceptor.class);

	//TODO: temporary hack to get SMART authenticated.. must connect to auth framework later...
	private static final HashMap<String, String> credentials = new HashMap<String, String>();

	public WssAuthInterceptor() {

		credentials.put("esatnar", "pass");
		credentials.put("smturm01soapp", "smturm01@3R!");

	}

	protected Subject createSubject(String name, String password, boolean isDigest, String nonce, String created) {
		Subject subject = new Subject();

		// delegate to the external security system if possible

		// authenticate the user somehow
		String storePassword = this.credentials.get(name);
		if (password == null) {
			logger.error("Username (" + name + ") not found authStore!!");
			return null;
		}
		if (!password.equals(storePassword)) {
			logger.error("Username (" + name + ") did not authenticate with the authStore!!");
			return null;
		}
		
		subject.getPrincipals().add(new SimplePrincipal(name));

		// add roles this user is in
		String roleName = "smartClient";
		subject.getPrincipals().add(new SimpleGroup(roleName, name));
		return subject;
	}



}

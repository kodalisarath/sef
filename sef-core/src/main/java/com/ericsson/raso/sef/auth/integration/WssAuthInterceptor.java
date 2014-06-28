package com.ericsson.raso.sef.auth.integration;

import java.util.HashMap;

import javax.security.auth.Subject;

import org.apache.cxf.common.security.SimpleGroup;
import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.cxf.common.security.UsernameToken;
import org.apache.cxf.interceptor.security.AbstractUsernameTokenInInterceptor;

public class WssAuthInterceptor extends AbstractUsernameTokenInInterceptor {

	//TODO: temporary hack to get SMART authenticated.. must connect to auth framework later...
	private static final HashMap<String, String> credentials = new HashMap<String, String>();

	public WssAuthInterceptor() {
		credentials.put("esatnar", "pass");
		credentials.put("smturm01soapp", "smturm01@3R!");

	}

	@Override
	protected Subject createSubject(UsernameToken usernameToken) {
		Subject subject = new Subject();

		// delegate to the external security system if possible

		// authenticate the user somehow
		String password = this.credentials.get(usernameToken.getName());
		if (password == null) {
			this.reportSecurityException("Username not found authStore!!");
			return null;
		}
		if (!password.equals(usernameToken.getPassword())) {
			this.reportSecurityException("Username did not authenticate with the authStore!!");
			return null;
		}
		
		subject.getPrincipals().add(new SimplePrincipal(usernameToken.getName()));

		// add roles this user is in
		String roleName = "smartClient";
		subject.getPrincipals().add(new SimpleGroup(roleName, usernameToken.getName()));
		return subject;
	}



}

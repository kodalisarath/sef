package com.ericsson.raso.sef.auth.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.ericsson.raso.sef.auth.Authorizations;
import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;
import com.ericsson.raso.sef.auth.PrivilegeManager;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;

public class AuthorizeMethod implements InvocationHandler {

	private Object proxied = null;
	PrivilegeManager manager = new PrivilegeManager("Z:\\Common Share\\Projects\\raso-cac\\rasocac\\privilegeStore.zccm");

	public AuthorizeMethod(Object proxied) {
		this.proxied = proxied;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		
		System.out.println("Working on method: " + method.getName());
		if (method.isAnnotationPresent(AuthorizeIfAllowedFor.class)) {
			System.out.println("Auth annotation present!!");
			Authorizations authPrincipal = method.getAnnotation(Authorizations.class);
			boolean isAuthorized = false;
			for (AuthorizeIfAllowedFor auth : authPrincipal.value()) {
				System.out.println("Permission: " + auth.permission());
				System.out.println("Reference Check: " + auth.referenceValueInIdentity());

				if (manager.readPermission("ACCESS_ALL").implies(new Permission(auth.permission())))
					isAuthorized = true;
				if (auth.equals(AuthorizationPrinciple.SERVICE_CTXT_PRIMITIVE)
						&& method.getName().equalsIgnoreCase("testAnnotation"))
					isAuthorized = true;
			}
		}
		return method.invoke(proxied, args);
	}

}

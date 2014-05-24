package com.ericsson.raso.sef.auth.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.ericsson.raso.sef.auth.Authorizations;
import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;
import com.ericsson.raso.sef.auth.PrivilegeManager;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;
import com.ericsson.raso.sef.auth.service.IPrivilegeManager;

public class AuthorizeMethod implements InvocationHandler {

	private Object proxied = null;
	IPrivilegeManager manager = new PrivilegeManager(
			"Z:\\Common Share\\Projects\\raso-cac\\rasocac\\privilegeStore.zccm");

	public AuthorizeMethod(Object proxied) {
		this.proxied = proxied;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		System.out.println("Proxy: " + this.getClass().getCanonicalName() + ", Proxied: "
				+ proxied.getClass().getCanonicalName());

		System.out.println("Working on method: " + method.getName());
		Method originalMethod = proxied.getClass().getMethod(method.getName(), method.getParameterTypes());
		Authorizations authPrincipal = originalMethod.getAnnotation(Authorizations.class);
		if (authPrincipal == null)
			System.out.println("Annotation not present!!");
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

//		Parameter[] params = originalMethod.getParameters();
//		if (params != null) {
//			for (Parameter param : params) {
//				System.out.println("Parameter: " + param.getName());
//				Authorizations paramAuthorizations = param.getAnnotation(Authorizations.class);
//				if (paramAuthorizations != null) {
//					for (AuthorizeIfAllowedFor auth : paramAuthorizations.value()) {
//						System.out.println("Permission: " + auth.permission());
//						System.out.println("Reference Check: " + auth.referenceValueInIdentity());
//					}
//				} else
//					System.out.println("Parameter has no annotation");
//			}
//		}

		// if (originalMethod.isAnnotationPresent(AuthorizeIfAllowedFor.class))
		// {
		// System.out.println("Auth annotation present!!");
		// Authorizations authPrincipal =
		// originalMethod.getAnnotation(Authorizations.class);
		// boolean isAuthorized = false;
		// for (AuthorizeIfAllowedFor auth : authPrincipal.value()) {
		// System.out.println("Permission: " + auth.permission());
		// System.out.println("Reference Check: " +
		// auth.referenceValueInIdentity());
		//
		// if (manager.readPermission("ACCESS_ALL").implies(new
		// Permission(auth.permission())))
		// isAuthorized = true;
		// if (auth.equals(AuthorizationPrinciple.SERVICE_CTXT_PRIMITIVE)
		// && method.getName().equalsIgnoreCase("testAnnotation"))
		// isAuthorized = true;
		// }
		// } else {
		// System.out.println("Annotation not found!!!!!!!!");
		// }
		return method.invoke(proxied, args);
	}

}

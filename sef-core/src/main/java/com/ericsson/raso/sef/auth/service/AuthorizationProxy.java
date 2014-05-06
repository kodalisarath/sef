package com.ericsson.raso.sef.auth.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import com.ericsson.raso.sef.auth.Actor;
import com.ericsson.raso.sef.auth.Algorithm;
import com.ericsson.raso.sef.auth.Authorizations;
import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;
import com.ericsson.raso.sef.auth.Credential;
import com.ericsson.raso.sef.auth.Credential.Type;
import com.ericsson.raso.sef.auth.CredentialFactory;
import com.ericsson.raso.sef.auth.Identity;
import com.ericsson.raso.sef.auth.PasswordCredential;
import com.ericsson.raso.sef.auth.User;
import com.ericsson.raso.sef.auth.UserStore;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;
import com.ericsson.raso.sef.auth.permissions.Privilege;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class AuthorizationProxy implements InvocationHandler {

	private Object proxied = null;
	private Class<?> proxiedInterface = null;

	// TODO: REquest Context Service

	private AuthorizationProxy(Object proxied, Class<?> annotatedInterface) {
		this.proxied = proxied;
		this.proxiedInterface = annotatedInterface;
	}

	public static Object getInstance(Object proxied, Class<?> annotatedInterface) {
		Object proxy = Proxy.newProxyInstance(AuthorizationProxy.class.getClassLoader(),
				new Class[] { annotatedInterface }, new AuthorizationProxy(proxied, annotatedInterface));

		return proxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		// TODO: Update code for references when Service COntext Resolvers are
		// complete
		RequestContext requestContext = null;
		IUserStore store = null;
		IPrivilegeManager privilegeStore = null;

		// TODO: testing code.... remove after Request Context code is ready....
		requestContext = new RequestContext();
		Actor actor = new Actor("Sathya");
		PasswordCredential credential = (PasswordCredential) CredentialFactory.getInstance(Type.PASSWORD);
		credential.setAlgorithm(Algorithm.BASIC);
		credential.setPassword("abcd1234@(");
		User user = new User("esatnar", credential);
		user.addPrivilege(privilegeStore.readPermission(AuthorizationPrinciple.ACCESS_ALL));
		user.addPrivilege(privilegeStore.readPermission(AuthorizationPrinciple.SERVICE_CTXT_PRIMITIVE));
		actor.addIdentity(user);
		requestContext.setActor(actor);

		store = new UserStore();
		// end of testing code.... remove after Request Context Service code is
		// ready....

		AuthorizeIfAllowedFor[] authorizations = this.getAuthorizationTokens(method);
		boolean isAuthorized = false;
		for (AuthorizeIfAllowedFor auth : authorizations) {
			System.out.println("Permission: " + auth.permission());
			System.out.println("Reference Check: " + auth.referenceValueInIdentity());

			Privilege requiredPermission = privilegeStore.readPermission(auth.permission());

			if (auth.referenceValueInIdentity() == null || auth.referenceValueInIdentity().isEmpty()) {
				Actor invoker = requestContext.getActor();
				if (invoker.isAuthorizedFor(requiredPermission))
					return method.invoke(proxied, args);
				else
					throw new SecurityException("Actor not authorized for use-case/access");
			} else {
				System.out.println("Reference value: '" + auth.referenceValueInIdentity() + "'");
				// TODO: Write code for getting relevant meta

				// TOFO: deal with domain specific entity logic - preferably
				// under delegated self-evaluation
				String actorPrimitives = store.fetchReferenceMeta(actor.getName(), auth.referenceValueInIdentity());
				if (actorPrimitives.contains(method.getName())) {
					// TODO: check for parameter values authorized...

					return method.invoke(proxied, args);
				} else
					throw new SecurityException("Actor not authorized for use-case/access");

			}

		}

		return method.invoke(proxied, args);
	}

	private AuthorizeIfAllowedFor[] getAuthorizationTokens(Method method) {
		ArrayList<AuthorizeIfAllowedFor> authList = new ArrayList<AuthorizeIfAllowedFor>();
		try {
			Method originalMethod = proxied.getClass().getMethod(method.getName(), method.getParameterTypes());
			Authorizations authorizations = originalMethod.getAnnotation(Authorizations.class);
			if (authorizations == null || authorizations.value() == null) {
				System.out.println("annotation not found on implementation level");

				originalMethod = proxiedInterface.getMethod(method.getName(), method.getParameterTypes());
				authorizations = originalMethod.getAnnotation(Authorizations.class);
				if (authorizations == null || authorizations.value() == null) {
					System.out.println("annotation not found in interface level...");
					return null;
				} else {
					System.out.println("annotation present at interface level....");
					for (AuthorizeIfAllowedFor temp: authorizations.value()) 
						authList.add(temp);
				}
			} else {
				for (AuthorizeIfAllowedFor temp: authorizations.value()) 
					authList.add(temp);
			}
		} catch (NoSuchMethodException e) {
			return null;
		}
		return (AuthorizeIfAllowedFor[]) authList.toArray();
	}

}

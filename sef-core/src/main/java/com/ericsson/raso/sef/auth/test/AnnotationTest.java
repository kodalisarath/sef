package com.ericsson.raso.sef.auth.test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import com.ericsson.raso.sef.auth.Authorizations;
import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;

public class AnnotationTest {

	public static void main(String[] args) {
		try {
			ServiceContext pojo = new AnnotatedPojo();
			AuthorizeMethod authorizeMethod = new AuthorizeMethod(pojo);
			pojo = (ServiceContext) Proxy.newProxyInstance(AuthorizeMethod.class.getClassLoader(), AnnotatedPojo.class.getInterfaces(), authorizeMethod);
			System.out.println("Attempting annotations test....");

			
			// validate the entity access
			for (Field f : pojo.getClass().getFields()) {
				System.out.println("\n\nProcessing Attribute: " + f.getName() + ", value = " + f.get(pojo));
				Authorizations authPrinciple = f.getAnnotation(Authorizations.class);
				if (authPrinciple != null) {
					for (AuthorizeIfAllowedFor auth : authPrinciple.value()) {

						System.out.println("Permission: " + auth.permission());
						System.out.println("Reference Check: " + auth.referenceValueInIdentity());
					}
				} else {
					System.out.println("Cannot get auth annotation...");
				}
			}
			
			// validate the usecase access
			pojo.testAnnotation("Achinthya");

		
		
		} catch (Exception e) {
			System.out.println("Exception: " + e.getClass().getCanonicalName() + " - " + e.getMessage());
			e.printStackTrace();
		}
	}

}

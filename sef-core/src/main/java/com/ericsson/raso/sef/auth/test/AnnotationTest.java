package com.ericsson.raso.sef.auth.test;

import java.lang.reflect.Field;

import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;

public class AnnotationTest {

	public static void main(String[] args) {
		try {
			AnnotatedPojo pojo = new AnnotatedPojo();

			for (Field f : pojo.getClass().getFields()) {
				System.out.println("Processing Attribute: " + f.getName()
						+ ", value = " + f.get(pojo));
				AuthorizeIfAllowedFor authPrinciple = f
						.getAnnotation(AuthorizeIfAllowedFor.class);
				if (authPrinciple != null) {
					System.out.println("Permission: " + authPrinciple.permission());
					System.out.println("Reference Check: " + authPrinciple.referenceValueInIdentity());
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getClass().getCanonicalName() + " - " + e.getMessage());
			e.printStackTrace();
		}
	}

}

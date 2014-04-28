package com.ericsson.raso.sef.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;

@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
@Repeatable(Authorizations.class)
public @interface AuthorizeIfAllowedFor {
	public abstract AuthorizationPrinciple permission();
	public abstract String referenceValueInIdentity();

}

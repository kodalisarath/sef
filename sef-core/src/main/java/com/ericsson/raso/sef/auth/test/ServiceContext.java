package com.ericsson.raso.sef.auth.test;

import static com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple.*;

import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;

public interface ServiceContext {

	//@AuthorizeIfAllowedFor(permission = ACCESS_UPDATE, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = SERVICE_CTXT_PRIMITIVE, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_SUBSCRIBER_ALL, referenceValueInIdentity = "")
	public void testAnnotation(@AuthorizeIfAllowedFor(
			permission = DOMAIN_SUBSCRIBER_META,
			referenceValueInIdentity = "name") String meta);

}

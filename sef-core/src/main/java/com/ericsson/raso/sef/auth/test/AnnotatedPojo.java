package com.ericsson.raso.sef.auth.test;

import static com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple.ACCESS_UPDATE;
import static com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple.DOMAIN_SUBSCRIBER_MARKET_OWN;
import static com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple.DOMAIN_SUBSCRIBER_META;
import static com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple.SERVICE_CTXT_PRIMITIVE;

import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;


public class AnnotatedPojo implements ServiceContext {

	@AuthorizeIfAllowedFor(permission = DOMAIN_SUBSCRIBER_MARKET_OWN, referenceValueInIdentity = "PLDT.SMART")
	@AuthorizeIfAllowedFor(permission = DOMAIN_SUBSCRIBER_META, referenceValueInIdentity = "name")
	public String	name	= "Sathya";

	@AuthorizeIfAllowedFor(permission = DOMAIN_SUBSCRIBER_MARKET_OWN, referenceValueInIdentity = "PLDT.SMART")
	@AuthorizeIfAllowedFor(permission = DOMAIN_SUBSCRIBER_META, referenceValueInIdentity = "gender")
	public String	gender	= "male";

	@AuthorizeIfAllowedFor(permission = DOMAIN_SUBSCRIBER_MARKET_OWN, referenceValueInIdentity = "PLDT.SMART")
	@AuthorizeIfAllowedFor(permission = DOMAIN_SUBSCRIBER_META, referenceValueInIdentity = "age")
	public int		age		= 33;

	@AuthorizeIfAllowedFor(permission = ACCESS_UPDATE, referenceValueInIdentity = "")
	@AuthorizeIfAllowedFor(permission = SERVICE_CTXT_PRIMITIVE, referenceValueInIdentity = "")
	public void testAnnotation(
			@AuthorizeIfAllowedFor(permission = DOMAIN_SUBSCRIBER_META, referenceValueInIdentity = "name") String name) {
		this.name = name;
		System.out.println("method executed successfully!!");
	}

}

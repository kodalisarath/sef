package test.com.ericsson.raso.sef.auth;

import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;

public class AnnotatedPojo {
	
	@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_SUBSCRIBER_MARKET_OWN , referenceValueInIdentity="PLDT.SMART")
	@AuthorizeIfAllowedFor(permission=AuthorizationPrinciple.DOMAIN_SUBSCRIBER_META, referenceValueInIdentity="name")
	private String name = "Sathya";
	
	@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_SUBSCRIBER_MARKET_OWN , referenceValueInIdentity="PLDT.SMART")
	@AuthorizeIfAllowedFor(permission=AuthorizationPrinciple.DOMAIN_SUBSCRIBER_META, referenceValueInIdentity="gender")
	private String gender = "male";
	
	@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_SUBSCRIBER_MARKET_OWN , referenceValueInIdentity="PLDT.SMART")
	@AuthorizeIfAllowedFor(permission=AuthorizationPrinciple.DOMAIN_SUBSCRIBER_META, referenceValueInIdentity="age")
	private int age = 33;
	

}

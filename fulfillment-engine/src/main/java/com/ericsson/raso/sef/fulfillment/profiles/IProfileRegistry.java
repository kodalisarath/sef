package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;

public interface IProfileRegistry {

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_CREATE_NEW, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract boolean createProfile(FulfillmentProfile<?> profile) throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_UPDATE, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract boolean updateProfile(FulfillmentProfile<?> profile) throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_DELETE, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract boolean deleteProfile(FulfillmentProfile<?> profile) throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_READ_ONLY, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract FulfillmentProfile<?> readProfile(String profile) throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_READ_ONLY, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract List<FulfillmentProfile<?>> getAllProfiles() throws CatalogException;
	
	

}
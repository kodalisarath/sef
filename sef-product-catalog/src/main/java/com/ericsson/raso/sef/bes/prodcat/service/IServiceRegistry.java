package com.ericsson.raso.sef.bes.prodcat.service;

import java.util.List;

import com.ericsson.raso.sef.auth.AuthorizeIfAllowedFor;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.Owner;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;

public interface IServiceRegistry {

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_CREATE_NEW, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract boolean createResource(Resource resource) throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_UPDATE, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract boolean updateResource(Resource resource) throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_DELETE, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract boolean deleteResource(Resource resource) throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_READ_ONLY, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract Resource readResource(String resource) throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_READ_ONLY, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract List<Resource> getAllResources() throws CatalogException;

	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_READ_ONLY, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract List<Resource> getAllResourcesFor(Owner owner) throws CatalogException;
	
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_READ_ONLY, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract List<Resource> getAllResourcesFor(boolean isDiscoverable) throws CatalogException;
	
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.ACCESS_READ_ONLY, referenceValueInIdentity = "")
	//@AuthorizeIfAllowedFor(permission = AuthorizationPrinciple.DOMAIN_PRODUCT_TECHNICAL_OWN, referenceValueInIdentity = "Owner")
	public abstract List<Resource> getAllResourcesFor(String resourceGroup) throws CatalogException;
	
	

}
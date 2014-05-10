package com.ericsson.raso.sef.bes.prodcat.service;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.Owner;

public interface IOwnerManager {

	public abstract boolean createOwner(Owner owner) throws CatalogException;

	public abstract Owner readOwner(String owner) throws CatalogException;

	public abstract boolean updateOwner(Owner owner) throws CatalogException;

	public abstract boolean deleteOwner(Owner owner) throws CatalogException;

}
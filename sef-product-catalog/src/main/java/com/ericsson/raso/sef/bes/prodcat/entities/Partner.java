package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class Partner extends Owner {
	private static final long serialVersionUID = -1727528342129511142L;

	public Partner(String name) {
		super(name);
		this.setType(Type.Partner);
	}

	@Override
	public void addOpco(Opco opco) throws CatalogException {
		throw new CatalogException("A Partner entity cannot add any members!!");
	}

	@Override
	public void addPartner(Partner partner) throws CatalogException {
		throw new CatalogException("A Partner entity cannot add any members!!");
	}

	@Override
	public void addTenant(TenantMvno mvno) throws CatalogException {
		throw new CatalogException("A Partner entity cannot add any members!!");
	}

	@Override
	public void removeOpco(Opco opco) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a partner cannot add
		 * any sub stakeholders, there is no need to remove it.
		 */	
	}

	@Override
	public void removePartner(Partner partner) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a partner cannot add
		 * any sub stakeholders, there is no need to remove it.
		 */	
	}

	@Override
	public void removeMarket(Market market) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a partner cannot add
		 * any sub stakeholders, there is no need to remove it.
		 */	
	}

	@Override
	public void removeTenant(TenantMvno mvno) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a partner cannot add
		 * any sub stakeholders, there is no need to remove it.
		 */	
	}

	@Override
	public void removeOpco(String opco) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a partner cannot add
		 * any sub stakeholders, there is no need to remove it.
		 */	
	}

	@Override
	public void removePartner(String partner) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a partner cannot add
		 * any sub stakeholders, there is no need to remove it.
		 */	
	}

	@Override
	public void removeMarket(String market) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a partner cannot add
		 * any sub stakeholders, there is no need to remove it.
		 */	
	}

	@Override
	public void removeTenant(String mvno) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a partner cannot add
		 * any sub stakeholders, there is no need to remove it.
		 */	
	}
	
	

}

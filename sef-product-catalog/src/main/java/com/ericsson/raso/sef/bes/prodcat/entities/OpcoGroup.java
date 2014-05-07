package com.ericsson.raso.sef.bes.prodcat.entities;

public class OpcoGroup extends Owner {
	private static final long serialVersionUID = -8241152233201632129L;

	public OpcoGroup(String name) {
		super(name);
		this.setType(Type.OpcoGroup);
	}

	@Override
	public void addTenant(TenantMvno mvno) throws CatalogException {
		throw new CatalogException("Cannot Add MVNO/Tenant: " + mvno + " to an OpcoGroup: " + this);
	}

	@Override
	public void removeTenant(TenantMvno mvno) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a tenant cannot be added to
		 * opcogroup, there is no need to remove it.
		 */
	}

	@Override
	public void removeTenant(String mvno) throws CatalogException {
		/*
		 * Deliberately no implementation.... Since a tenant cannot be added to
		 * opcogroup, there is no need to remove it.
		 */
	}

}

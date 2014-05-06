package com.ericsson.raso.sef.auth.service;

import com.ericsson.raso.sef.auth.AuthAdminException;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Privilege;

public interface IPrivilegeManager {

	public abstract boolean createPermission(Privilege privilege) throws AuthAdminException;

	public abstract Privilege readPermission(AuthorizationPrinciple principle) throws AuthAdminException;

	public abstract Privilege readPermission(String principle) throws AuthAdminException;

	public abstract boolean updatePermission(Privilege privilege) throws AuthAdminException;

	public abstract boolean deletePermission(Privilege privilege) throws AuthAdminException;

}
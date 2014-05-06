package com.ericsson.raso.sef.auth.test;

import com.ericsson.raso.sef.auth.AuthAdminException;
import com.ericsson.raso.sef.auth.PrivilegeManager;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;
import com.ericsson.raso.sef.auth.permissions.Privilege;
import com.ericsson.raso.sef.auth.service.IPrivilegeManager;

public final class AuthTest {

	public static void main(String[] args) {

		String privilegeStore = "Z:\\Common Share\\Projects\\raso-cac\\rasocac\\privilegeStore.zccm";
		
		IPrivilegeManager manager = new PrivilegeManager(privilegeStore);
		try {
			//manager.createPermission(new Permission(AuthorizationPrinciple.ACCESS_ALL, null));
			System.out.println("Reading from Store: " + manager.readPermission("ACCESS_ALL"));
			Privilege accessAll = manager.readPermission("ACCESS_ALL");
			Privilege accessUpdate = manager.readPermission("ACCESS_UPDATE");
			System.out.println("Imples Test: " + accessAll.implies(accessUpdate));
		} catch (AuthAdminException e) {
			e.printStackTrace();
		}
		
	}

}

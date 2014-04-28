package com.ericsson.raso.sef.auth.test;

import com.ericsson.raso.sef.auth.AuthAdminException;
import com.ericsson.raso.sef.auth.PrivilegeManager;
import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;

public final class AuthTest {

	public static void main(String[] args) {

		String privilegeStore = "Z:\\Common Share\\Projects\\raso-cac\\rasocac\\privilegeStore.zccm";
		
		PrivilegeManager manager = new PrivilegeManager(privilegeStore);
		try {
			manager.createPermission(new Permission(AuthorizationPrinciple.ACCESS_ALL, null));
			System.out.println("Reading from Store: " + manager.readPermission("ACCESS_ALL"));
		} catch (AuthAdminException e) {
			e.printStackTrace();
		}
		
	}

}

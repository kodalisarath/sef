package com.ericsson.raso.sef.auth.permissions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Permission implements Privilege, Serializable {


	private static final long serialVersionUID = -6643567321985656767L;
	
	private AuthorizationPrinciple			name				= null;
	private String			referenceInIdentity	= null;
	private List<Privilege>	impliedPermissions	= null;

	public Permission(AuthorizationPrinciple name) {
		this.name = name;
		this.impliedPermissions = new ArrayList<Privilege>();
	}

	public Permission(AuthorizationPrinciple name, String referenceInIdentity) {
		this.name = name;
		this.referenceInIdentity = referenceInIdentity;
		this.impliedPermissions = new ArrayList<Privilege>();
	}

	@Override
	public boolean contains(Privilege other) {
		if (this.name.equals(other.getName())) 
			return false;
		
		if (this.impliedPermissions == null)
			return false;
		
		if (this.impliedPermissions.contains(other))
			return true;
		
		return false;
	}
	
	@Override
	public boolean implies(Privilege other) {
		if (this.name.equals(other.getName()))
			return true;
		
		if (this.impliedPermissions == null)
			return false;
		
		if (this.impliedPermissions.contains(other)) 
			return true;

		Iterator<Privilege> privileges = this.impliedPermissions.iterator();
		while (privileges.hasNext()) {
			if (privileges.next().implies(other))
				return true;
		}
		
		//TODO: Logger - ideally this is dead code as per logic... but to be safe, add a logger to identify loopholes
		return false;
	}


	@Override
	public boolean addImplied(Privilege other) {
		if (this.impliedPermissions.contains(other)) {
			//TODO: Seems to be an update operation....
			this.impliedPermissions.remove(other);
		}
			return this.impliedPermissions.add(other);
	}

	@Override
	public boolean removeImplied(Privilege other) {
		return impliedPermissions.remove(other);
	}

	@Override
	public AuthorizationPrinciple getName() {
		return name;
	}

	@Override
	public String getReferenceInIdentity() {
		return this.referenceInIdentity;
	}

	@Override
	public String toString() {
		return "<Permission name=\"" + name + "\" referenceInIdentity=\""
				+ referenceInIdentity + "\"> <impliedPermissions>"
				+ impliedPermissions + "</impliedPermissions></Permission>";
	}
	
	
	
}

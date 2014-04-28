package com.ericsson.raso.sef.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ericsson.raso.sef.auth.permissions.Privilege;


/**
 * Representation of a real world user that can access the system with real world roles and responsibilities.
 * 
 * An actor can have multiple <code>Identity</code>, that can be authenticated through any form of <code>Credential</code>.
 * 
 * Irrespective of which <code>Identity</code> was used to identify & authenticate, an actor's role and his access control must
 * not change and this framework attempts to realize exactly that.
 * 
 * <strong>Important Note:</strong> The security domain keeps changing and evolving constantly. It will be impossible to support
 * all forms of cryptographic security from this framework. But there will be a constant effort to bring relevant cyptogaphic 
 * algorithms/ protocols as much as possible, but based on real world needs of our Customers.
 * 
 * In this framework, only PasswordCredentials are supported with the following encryption support...
 * <li>Base64</li>
 * <li>DES</li>
 * <li>MD5 Digest - typically used for robotic accounts/ identities to validate shared secrets</li>
 * <li>SHA1 Digest - typically used for robotic accounts/ identities to validate shared secrets</li>
 * <li>HVAC - Implementation is subject to realization</li>
 * <li>PIN_OTP - Implementation is subject to realization</li>
 * <li>MSISDN - Weak Authentication used only under network context when using mobile identity, where native telecom protocols do not support carrying credential parameters</li>
 * <li>IMSI - Weak Authentication used only under network context when using mobile identity, where native telecom protocols do not support carrying credential parameters</li>
 * 
 * 
 * @author esatnar
 *
 */
public class Actor implements Serializable {
	
	private String name = null;
	private Map<String, User> identities = null;
	private Map<String, Group> memberships = new HashMap<String, Group>();
	
	public Actor(String name) {
		this.name = name;
	}
	
	public User fetchIdentity(String name) {
		if (this.identities == null)
			return null;
		
		return this.identities.get(name);
	}
	
	public boolean isAuthorizedFor(Privilege privilege) {
		Privilege ownPrivilege = null;
		
		// check for permissions on user level first....
		Iterator<User> identities = this.identities.values().iterator();
		if (identities == null)
			return false;
		
		Identity identity = null;
		while (identities.hasNext()) {
			identity = identities.next();
			Iterator<Privilege> privileges = identity.getPrivileges().iterator();
			if (privileges == null)
				return false;
			
			while (privileges.hasNext()) {
				ownPrivilege = privileges.next();
				if (ownPrivilege.implies(privilege))
					return true;
			}
		}
		
		// seems like we need to check inherited permissions from memberships...
		Iterator<Group> groups = this.memberships.values().iterator();
		if (groups == null)
			return false;
		
		Group group = null;
		while (groups.hasNext()) {
			group = groups.next();
			Iterator<Privilege> privileges = group.getPrivileges().iterator();
			if (privileges == null)
				return false;
			
			while (privileges.hasNext()) {
				ownPrivilege = privileges.next();
				if (ownPrivilege.implies(privilege))
					return true;
			}
		}
		
		// if you are here, you are sure unlucky... innit? 
		return false;
		
	}
	
	public boolean addIdentity(User userIdentity) throws AuthAdminException {
		if (this.identities == null) {
			this.identities = new HashMap<String, User>();
		}
		
		if (this.identities.containsKey(userIdentity.getName())) {
			throw new AuthAdminException("Identity (" + userIdentity.getName() + ") already exists!!");
		}
		
		this.identities.put(userIdentity.getName(), userIdentity);
		return true;
	}
	
	
	/**
	 * Removes an identity from the Actor...
	 * 
	 * This is an administrative method only. Care must be taken when invoking this method, since this is expected to be the last operation on Actor's life-cycle. Changes made by this method is irreversible.
	 * 
	 * @param userIdentity
	 * @return <code>true</code> only when the operation is successful.
	 * @throws AuthAdminException - when the operation fails. Stacktrace (<code>Throwable<code>) is attached wherever applicable so source of problem can be analyzed.
	 */
	public boolean removeIdentity(User userIdentity) throws AuthAdminException {
		if (this.identities == null) {
			throw new AuthAdminException("Identity (" + userIdentity.getName() + ") doesnt exist!!");
		}
		
		if (!this.identities.containsKey(userIdentity.getName())) {
			throw new AuthAdminException("Identity (" + userIdentity.getName() + ") doesnt exist!!");
		} else {
			this.identities.remove(userIdentity);
			return true;
		}
		
//		if (this.memberships == null || this.memberships.isEmpty()) {
//			Iterator<Group> groups = this.memberships.values().iterator();
//			while (groups.hasNext()) {
//				Group group = groups.next();
//				group.removeMember(this);
//			}
//		}
		
		
		
	}
	

	public boolean isMemberOf(String groupName) {
		if (this.memberships == null || this.memberships.isEmpty())
			return false;
		
		if (this.memberships.containsKey(groupName))
			return true;
		else
			return false;
	}
	
	public boolean addMembership(Group group) {
		if (group == null)
			return false;
		
		if (this.memberships == null)
			this.memberships = new HashMap<String, Group>();
		
		if (!this.memberships.containsKey(group.getName()))
			this.memberships.put(group.getName(), group);
		
		group.addMember(this);
		
		return true;
	}
	
	public boolean removeMembership(Group group) {
		if (group == null)
			return false;
		
		if (this.memberships == null)
			return false;
		
		if (!this.memberships.containsKey(group.getName()))
			return false;
			
		this.memberships.remove(group.getName(), group);
		
		group.removeMember(this);
		
		return true;
	}
}

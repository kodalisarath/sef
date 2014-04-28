package com.ericsson.raso.sef.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.raso.sef.auth.permissions.Privilege;

/**
 * Representation of a real world user that can access the system with real world roles and responsibilities.
 * 
 * An real world user can have multiple <code>Identity</code>, that can be authenticated through any form of <code>Credential</code>.
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
public abstract class Identity implements Serializable {
	
	private String name = null;
	private List<Privilege> privileges = null;
	
	public Identity(String name) {
		this.name = name;
	}

	public boolean addPrivilege(Privilege privilege) {
		if (this.privileges == null)
			this.privileges = new ArrayList<Privilege>();
		
		return this.privileges.add(privilege);
	}
	
	public boolean removePrivilege(Privilege privilege) {
		if (this.privileges == null)
			return false;
		
		return this.privileges.remove(privilege);
	}
	
	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}
	
	public List<Privilege> getPrivileges() {
		return this.privileges;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}

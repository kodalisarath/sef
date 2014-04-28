package com.ericsson.raso.sef.auth.permissions;

import com.ericsson.raso.sef.auth.AuthAdminException;

public interface Privilege {
	
	/**
	 * Checks if privilege specified by <other> is implied by virtue of privileges specified in this object.
	 * 
	 * This method provides evaluation of permissions when explicit match is not available when authorizing a 
	 * particular access/ service context.
	 * 
	 * @param other - an instance of type <code>Privilege</code> to evaluate.
	 * @return <code>true</code> - if this permission does imply <code>other</code>. <code>false</code> otherwise.
	 */
	public abstract boolean implies(Privilege other);
	
	/**
	 * Checks if privilege specified by <code>other</code> is within immediate set of privileges. Unlike <code>implies()
	 * </code> method, this method does not iterate thru the entire hierarchy of the Permissions.
	 * 
	 * This method is more of an administrative purpose and hence must be used in evaluation context, with a semantic 
	 * synonimous to <code>implies()</code>.
	 * 
	 * @param other - an instance of type <code>Privilege</code> to evaluate.
	 * @return <code>true</code> - if this permission does contains <code>other</code>. <code>false</code> otherwise.
	 */
	public abstract boolean contains(Privilege other);
	
	
	/**
	 * Allows managing a <code>Privilege</code> in terms of its relationship with other <code>Privilege</code> instance.
	 * 
	 * This method will add the <code>other</code> as an implied <code>Privilege</code>
	 * 
	 * @param other - an instance of type <code>Privilege<code> to relate to.
	 * @return
	 * @throws AuthAdminException 
	 * @see implies(), removeImplied()
	 */
	public abstract boolean addImplied(Privilege other) throws AuthAdminException;
	
	
	/**
	 * Allows managing a <code>Privilege</code> in terms of its relationship with other <code>Privilege</code> instance.
	 * 
	 * This method will remove the <code>other</code> as an implied <code>Privilege</code>
	 * 
	 * @param other - an instance of type <code>Privilege<code> to relate to.
	 * @return
	 * @see implies()
	 */
	public abstract boolean removeImplied(Privilege other);

	/**
	 * Marker method to enforce implementations to provide an identity token to the Privilege.
	 * 
	 * An <code>Enum</code> is provided to ensure compile-time uniqueness guarantee & preempt any considerations to 
	 * impact in design of new services, entity models, etc.
	 * 
	 * @return name - an identity for the implemented <code>Privilege</code>
	 * @see AuthorizationPrinciple
	 */
	public abstract AuthorizationPrinciple getName();
	
	/**
	 * Marker method to enforce implementations to provide an preempt/prompt to consider reference values in the 
	 * authenticated <code>Identity</code>, that are required for evaluating an authorization context.
	 * 
	 * For certain types of <code>Privilege</code> implementations, this may not be required. For example, a super user
	 * would have privileges and hence would not require context specific info security principles to execute.
	 * 
	 * <strong>Important:</strong> While this parameter is defined optional, the designers and developers must 
	 * definitely consider the use from a trust perspective. Use of such features, will avoid designing/ implementing 
	 * infrastructural code as a functional requirements and thus creating multiple service variants that are only 
	 * marginally different from each other. 
	 * 
	 * An <code>Enum</code> is provided to ensure compile-time uniqueness guarantee & preempt any considerations to 
	 * impact in design of new services, entity models, etc.
	 * 
	 * @return name - an identity for the implemented <code>Privilege</code>
	 * @see AuthorizationPrinciple
	 */
	public abstract String getReferenceInIdentity();

	
}

package com.ericsson.raso.sef.auth.mapper;

import com.ericsson.raso.sef.auth.AuthAdminException;
import com.ericsson.raso.sef.auth.Group;
import com.ericsson.raso.sef.auth.User;
import com.ericsson.raso.sef.auth.permissions.Privilege;

public interface ActorMapper {

	public User fetchIdentity(String name);

	public boolean isAuthorizedFor(Privilege privilege);

	public boolean addIdentity(User userIdentity) throws AuthAdminException;

	public boolean removeIdentity(User userIdentity);

	public boolean isMemberOf(String groupName);

	public boolean addMembership(Group group);

	public boolean removeMembership(Group group);
	
}

package com.ericsson.raso.sef.auth.service;

import com.ericsson.raso.sef.auth.Actor;
import com.ericsson.raso.sef.auth.Credential;
import com.ericsson.raso.sef.auth.Group;
import com.ericsson.raso.sef.auth.User;
import com.ericsson.raso.sef.auth.permissions.Privilege;

public interface AuthService {
	
	public User fetchIdentity(String name);

	public boolean isAuthorizedFor(Privilege privilege);

	public boolean addIdentity(User userIdentity);

	public boolean removeIdentity(User userIdentity);

	public boolean isMemberOf(String groupName);

	public boolean addMembership(Group group);

	public boolean removeMembership(Group group);
	
	public boolean addMember(Actor actor);

	public boolean removeMember(Actor actor);

	public Credential getCredential();

	public void setCredential(Credential credential);

}

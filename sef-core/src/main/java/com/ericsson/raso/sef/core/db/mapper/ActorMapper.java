package com.ericsson.raso.sef.core.db.mapper;

import com.ericsson.raso.sef.auth.Actor;
import com.ericsson.raso.sef.auth.Group;
import com.ericsson.raso.sef.auth.User;
import com.ericsson.raso.sef.auth.permissions.Privilege;
import com.ericsson.raso.sef.core.FrameworkException;

public interface ActorMapper {

	public User fetchIdentity(String name);
	
	public String getGroupName(Actor actor);

	public boolean isAuthorizedFor(Privilege privilege);
	
	public Actor readActor(String actorName);
	
	public void createActor(Actor actor);
	
	public void deleteActor(Actor actor);
	
	public void createMapping(Actor actor);
	
	public void updateMetas(Actor actor);

	public boolean isMemberOf(String groupName);

	public void addMembership(Group group);

	public void removeMembership(Group group);
	
	public String fetchReferenceMeta(String actorName, String metaName)
			throws FrameworkException;

}
	
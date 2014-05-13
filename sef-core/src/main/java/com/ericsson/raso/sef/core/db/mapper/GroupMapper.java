package com.ericsson.raso.sef.core.db.mapper;

import java.util.List;

import com.ericsson.raso.sef.auth.Actor;
import com.ericsson.raso.sef.auth.Group;

public interface GroupMapper {

	public void addMember(Actor actor);
	
	public List<Group> getAllGroups();
	
	public Group getGroupById(Integer groupId);

	public void removeMember(Actor actor);

	public void insertGroup(Group group); 
	
	public void deleteGroup(Group group);

	public void updateGroup(Group group);

	public String readGroup(String groupName);
	
}

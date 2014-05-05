package com.ericsson.raso.sef.auth.mapper;

import com.ericsson.raso.sef.auth.Actor;

public interface GroupMapper {

	public boolean addMember(Actor actor);

	public boolean removeMember(Actor actor);

	
}

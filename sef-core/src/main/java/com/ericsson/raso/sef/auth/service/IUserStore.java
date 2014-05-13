package com.ericsson.raso.sef.auth.service;

import com.ericsson.raso.sef.auth.Actor;
import com.ericsson.raso.sef.auth.Group;
import com.ericsson.raso.sef.core.FrameworkException;

public interface IUserStore {

	public abstract boolean createActor(Actor actor) throws FrameworkException;

	public abstract boolean updateActor(Actor actor) throws FrameworkException;

	public abstract boolean deleteActor(Actor actor) throws FrameworkException;

	public abstract Actor readActor(String actorName) throws FrameworkException;
	
	public abstract String fetchReferenceMeta(String actorName, String metaName) throws FrameworkException;

	public abstract boolean createGrop(Group group) throws FrameworkException;

	public abstract boolean updateGroup(Group group) throws FrameworkException;

	public abstract boolean deleteGroup(Group group) throws FrameworkException;

	public abstract String readGroup(String groupName) throws FrameworkException;

	
}
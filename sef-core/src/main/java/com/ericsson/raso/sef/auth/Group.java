package com.ericsson.raso.sef.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of <code>Identity</code> interface to represent an administrative unit representing a collection of <code>User</code> 
 * 
 * @author esatnar
 *
 */
public class Group extends Identity {
	
	private String name = null;
	private List<Actor> members = null;
	
	public Group(String name) {
		super(name);
	}
	
	public boolean addMember(Actor actor) {
		if (actor == null)
			return false;
		
		if (this.members == null) 
			this.members = new ArrayList<Actor>();
		
		return this.members.add(actor);
	}

	public boolean removeMember(Actor actor) {
		if (actor == null)
			return false;
		
		if (this.members == null) 
			return false;
		
		return this.members.remove(actor);
	}

	
}

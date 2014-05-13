package com.ericsson.raso.sef.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of <code>Identity</code> interface to represent an
 * administrative unit representing a collection of <code>User</code>
 * 
 * @author esatnar
 *
 */
public class Group extends Identity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 962983347687330874L;
	
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Actor> getMembers() {
		return members;
	}

	public void setMembers(List<Actor> members) {
		this.members = members;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Group [description=" + description + ", members=" + members
				+ "]";
	}

}

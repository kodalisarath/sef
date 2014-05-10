package com.ericsson.raso.sef.bes.prodcat.entities;

public final class EndUser extends Owner {
	private static final long serialVersionUID = -5413245170445372849L;

	public EndUser(String name) {
		super(name);
		this.setType(Type.EndUser);
	}


}

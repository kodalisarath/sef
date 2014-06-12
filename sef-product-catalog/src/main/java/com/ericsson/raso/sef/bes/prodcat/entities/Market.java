package com.ericsson.raso.sef.bes.prodcat.entities;

public final class Market extends Owner {
	private static final long serialVersionUID = -7819516327755652080L;

	public Market(String name) {
		super(name);
		this.setType(Type.Market);
	}

	@Override
	public String toString() {
		return "Market [getType()=" + getType() + ", getName()=" + getName() + "]";
	}


}

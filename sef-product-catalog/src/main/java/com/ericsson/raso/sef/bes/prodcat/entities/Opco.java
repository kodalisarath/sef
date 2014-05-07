package com.ericsson.raso.sef.bes.prodcat.entities;

public class Opco extends Owner {
	private static final long serialVersionUID = 2283029726710283881L;

	public Opco(String name) {
		super(name);
		this.setType(Type.Opco);
	}


}

package com.ericsson.raso.sef.core.db.model;

public enum ContractState {
	PREACTIVE("PRE_ACTIVE"),
	ACTIVE("ACTIVE"),
	RECYCLED("DEACTIVE"),
	BARRED("SUSPENDED"),
	GRACE("GRACE"),
	READY_TO_DELETE("READY_TO_DELETE"),
	DUNNING ("DELETED"),
	NONE("None");

	private String name;

	private ContractState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static ContractState apiValue(String name) {

		if (name.equalsIgnoreCase("PRE_ACTIVE")) {
			return PREACTIVE;
		} else if (name.equalsIgnoreCase("ACTIVE")) {
			return ACTIVE;
		} else if (name.equalsIgnoreCase("DEACTIVE")) {
			return RECYCLED;
		} else if (name.equalsIgnoreCase("SUSPENDED")) {
			return BARRED;
		} else if (name.equalsIgnoreCase("GRACE")) {
			return GRACE;
		} else if (name.equalsIgnoreCase("READY_TO_DELETE")) {
			return READY_TO_DELETE;
		} else if (name.equalsIgnoreCase("DELETED")) {
			return DUNNING;
		} else {
			return NONE;
		}
	}
}

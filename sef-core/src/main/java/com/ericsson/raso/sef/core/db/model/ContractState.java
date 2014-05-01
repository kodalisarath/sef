package com.ericsson.raso.sef.core.db.model;

public enum ContractState {
	PREACTIVE("PreActive"), ACTIVE("Active"), RECYCLED("Recycle"), GRACE("Grace"), READY_TO_DELETE("Ready To Delete"), NONE("None");
	
	private String name;

	private ContractState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

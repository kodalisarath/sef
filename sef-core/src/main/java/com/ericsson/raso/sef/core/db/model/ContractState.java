package com.ericsson.raso.sef.core.db.model;

public enum ContractState {
	PREACTIVE("PRE_ACTIVE"), ACTIVE("ACTIVE"), RECYCLED("DEACTIVE"), BARRED("SUSPENDED"), GRACE("GRACE"), READY_TO_DELETE("READY_TO_DELETE"), NONE("None");
	
	private String name;

	private ContractState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

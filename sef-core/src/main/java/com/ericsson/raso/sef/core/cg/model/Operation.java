package com.ericsson.raso.sef.core.cg.model;


public enum Operation  {

	RESERVE(Type.TRANSACATION_START, "1"),
	COMMIT(Type.TRANSACTION_END, "2"),
	CANCEL(Type.TRANSACTION_END, "9"),
	DIRECT_DEBIT(Type.NO_TRANSACTION, "3"),
	TA_STATE(Type.INTERMEDIATE_TRANSACTION, "8"),
	ACCOUNT_LIST(Type.NO_TRANSACTION, "6"),
	MMS_CHARGING(Type.NO_TRANSACTION, ""),
	SMS_CHARGING_INITIAL(Type.TRANSACATION_START, ""),
	SMS_CHARGING_TERMMINATE(Type.TRANSACTION_END, "");

	private Type type;
	private String name;
	
	private Operation(Type type, String name) {
		this.type = type;
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static Operation toOperation(String op) {
		Operation[] operations = Operation.values();
		for (Operation operation : operations) {
			if(operation.getName().equals(op)) {
				return operation;
			}
		}
		return null;
	}
	
	public static enum Type {
		TRANSACATION_START, TRANSACTION_END, INTERMEDIATE_TRANSACTION, NO_TRANSACTION
	}
}

package com.ericsson.raso.sef.smart.usecase;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;

public abstract class SmartRequest {
	
	public static final String CUSTOMER_ID = "CustomerId";
	public static final String ACCESS_KEY = "AccessKey";
	public static final String OWING_CUSTOMER_ID = "OwningCustomerId";
	public static final String KEY = "Key";

	private Usecase usecase;
	private boolean transactional;
	
	public Usecase getUsecase() {
		return usecase;
	}

	public void setUsecase(Usecase usecase) {
		this.usecase = usecase;
	}

	public boolean isTransactional() {
		return transactional;
	}

	public void setTransactional(boolean transactional) {
		this.transactional = transactional;
	}
	
	public String getCommand() {
		String command = usecase.getOperation();
		if(usecase.getModifier() != null) {
			command += ":" + usecase.getModifier();
		}
		return command;
	}
	
	public abstract void prepareRequest(Operation operation);
}

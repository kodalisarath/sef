package com.ericsson.sef.chargingadapter.usecase;

import org.apache.camel.Processor;

import com.ericsson.sef.chargingadapter.CaContext;
import com.ericsson.sef.chargingadapter.processor.ChargeAmountProcessor;

public enum Usecase {
	
	CHARGE_AMOUNT("chargeAmount", ChargeAmountProcessor.class);
	
	private String operation;
	private Class<? extends Processor> requestProcessorType;
	
	Usecase(String operation,Class<? extends Processor> requestProcessorType) {
		this.operation = operation;
		this.requestProcessorType = requestProcessorType;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public static Usecase getUsecaseByOperation(String operation) {
		for (Usecase uc : Usecase.values()) {
			if (uc.getOperation().equalsIgnoreCase(operation)) {
				return uc;
			}
		}
		return null;
	}
	
	public Processor getRequestProcessor() {
		return CaContext.getBean(requestProcessorType);
	}
}

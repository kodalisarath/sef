package com.ericsson.raso.sef.cg.engine.usecase;

import org.apache.camel.Processor;

import com.ericsson.raso.sef.cg.engine.processor.bizlogic.CancelAmountProcessor;
import com.ericsson.raso.sef.cg.engine.processor.bizlogic.ChargeAmountProcessor;
import com.ericsson.raso.sef.cg.engine.processor.bizlogic.CommitAmountProcessor;
import com.ericsson.raso.sef.cg.engine.processor.bizlogic.GetConsumerAccountlistProcessor;
import com.ericsson.raso.sef.cg.engine.processor.bizlogic.GetTAStateProcessor;
import com.ericsson.raso.sef.cg.engine.processor.bizlogic.MMSChargingProcessor;
import com.ericsson.raso.sef.cg.engine.processor.bizlogic.ReserveAmountProcessor;
import com.ericsson.raso.sef.cg.engine.processor.bizlogic.SMSChargingProcessor;

public enum Usecase {
	
	RESERVE("RESERVE", new ReserveAmountProcessor()),
	COMMIT("COMMIT", new CommitAmountProcessor()),
	DIRECT_DEBIT("DIRECT_DEBIT", new ChargeAmountProcessor()),
	TA_STATE("TA_STATE", new GetTAStateProcessor()),
	CANCEL("CANCEL", new CancelAmountProcessor()),
	ACCOUNT_LIST("ACCOUNT_LIST", new GetConsumerAccountlistProcessor()),
	MMS_CHARGING("MMS_CHARGING", new MMSChargingProcessor()),
	SMS_CHARGING_INITIAL("SMS_CHARGING_INITIAL", new SMSChargingProcessor()),
	SMS_CHARGING_TERMMINATE("SMS_CHARGING_TERMMINATE", new SMSChargingProcessor());
	
	private String operation;
	private Processor processor;
	
	Usecase(String operation,Processor processor) {
		this.operation = operation;
		this.processor = processor;
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
	
	public Processor getProcessor() {
		return processor;
	}
	
}

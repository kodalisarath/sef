package com.ericsson.raso.sef.logging;


public class Clear extends SnmpEntity {

	public Clear(int code, String message, String additionalMessage) {
		super(code, message, additionalMessage);
		this.snmp = LoggingHelper.getSendClear();
	}

}

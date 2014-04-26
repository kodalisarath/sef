package com.ericsson.raso.sef.logging;


public class Alarm extends SnmpEntity {

	public Alarm(int code, String message, String additionalMessage) {
		super(code, message, additionalMessage);
		this.snmp = LoggingHelper.getSendAlarm();
	}

}

package com.ericsson.raso.sef.logging;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;


public class LoggingHelper {
	
	private static final Marker SNMP = MarkerManager.getMarker("SNMP");
	
	private static final Marker SNMP_TRAP = MarkerManager.getMarker("TRAP", SNMP);
	private static final Marker SEND_ALARM = MarkerManager.getMarker("ALARM", SNMP_TRAP);
	private static final Marker SEND_CLEAR = MarkerManager.getMarker("CLEAR", SNMP_TRAP);

	private static final Marker SNMP_NOTIFICATION = MarkerManager.getMarker("NOTIFICATION", SNMP);
	private static final Marker SNMP_GET = MarkerManager.getMarker("GET", SNMP);
	private static final Marker SNMP_SET = MarkerManager.getMarker("SET", SNMP);
	
	public static Marker getSnmp() {
		return SNMP;
	}
	
	public static Marker getSnmpTrap() {
		return SNMP_TRAP;
	}
	
	public static Marker getSendAlarm() {
		return SEND_ALARM;
	}
	
	public static Marker getSendClear() {
		return SEND_CLEAR;
	}
	
	public static Marker getSnmpNotification() {
		return SNMP_NOTIFICATION;
	}
	
	public static Marker getSnmpGet() {
		return SNMP_GET;
	}
	
	public static Marker getSnmpSet() {
		return SNMP_SET;
	}

	 
	
}

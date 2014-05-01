package com.ericsson.raso.sef.logging;


import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LoggerService {
	
	public static final Marker SNMP = MarkerFactory.getMarker("SNMP");
	public static final Marker SNMP_TRAP = MarkerFactory.getMarker("TRAP");
	public static final Marker SNMP_CLEAR = MarkerFactory.getMarker("CLEAR");
	
	
	static{
		SNMP_TRAP.add(SNMP);
		SNMP_CLEAR.add(SNMP);
	}
	
	public static Marker getSnmpMarker() {
		return SNMP;
	}
	
	public static Marker getSnmpTrapMarker() {
		return SNMP_TRAP;
	}
	
	public static Marker getSnmpClearMarker() {
		return SNMP_CLEAR;
	}
	
}

package com.ericsson.raso.sef.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates a unique ID based on the following algorithm <br/><br/>
 * 
 * <i>ID = [node_name] + [sequence_counter] + [time_low] + [time_high] </i> <br/><br/>
 * 
 * <STRONG>node_name</STRONG>: Host id retrieved from system environment variable "hostname". Must be passed otherwise defaulted to "SEF_HOST" and uniqueness cannot be guaranteed across machines <br/>
 * <STRONG>sequence_counter</STRONG>: Atomic integer from Java <br/>
 * <STRONG>time_low</STRONG>: least significant 3 bytes from system time in nano seconds <br/>
 * <STRONG>time_high</STRONG>: most significant 3 bytes from system time in nano seconds <br/><br/>
 * 
 * Uniqueness of ID is guaranteed through the atomic counter within nano seconds
 * Node name is pre-fixed to guarantee uniqueness across JVMs 
 * 
 * How to use:
 * 
 * String uid = UniqueIdGenerator.generateId();
 * 
 * @author eljasan
 *
 */
public class UniqueIdGenerator {
	
	private static AtomicInteger counter = new AtomicInteger();
	private static String node = (String) System.getenv("HOSTNAME");

	
	public static String generateId() {
		if (node == null) {
			node = (String) System.getenv("HOSTNAME");
		}
		if(node == null) node="SEF_HOST";
		long time = System.nanoTime();
		String id = node + counter.incrementAndGet() + getTime_low(time) + getTime_high(time);
		return id;
	}
	
	public static int getTime_low(long l) {
		return (int) (0x0000000000ffffff & l);
	}
	
	public static long getTime_high(long l) {
		return ((0xffffff0000000000L & l) >> 80);
	}

}

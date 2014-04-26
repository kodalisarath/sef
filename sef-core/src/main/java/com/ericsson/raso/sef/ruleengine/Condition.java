package com.ericsson.raso.sef.ruleengine;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

/**
 * @author esatnar
 * 
 *         This is a marker interface for the rule engine semantics. The custom
 *         rule engine is designed over ECA (Event-Condition-Action)
 *         Architectural Pattern.
 * 
 */

public abstract class Condition implements Serializable {

	private static final long serialVersionUID = 5699271563042347480L;

	private ConditionType type = null;
	private List<String> values;

	public abstract boolean evaluate(Object operand) throws RuleFailedException;

	public ConditionType getType() {
		return type;
	}

	public void setType(ConditionType type) {
		this.type = type;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public abstract int hashCode();

	public abstract boolean equals(Object obj);

	public abstract String toString();

	protected static long ipToLong(InetAddress ip) {
		byte[] octets = ip.getAddress();
		long result = 0;
		for (byte octet : octets) {
			result <<= 8;
			result |= octet & 0xff;
		}
		return result;
	}
	
	public enum ConditionType {
		ENUMERATED, EXCLUSION_LIST, RANGE, NOT_IN_RANGE, STARTS_WITH, DOESNT_START_WITH, ENDS_WITH, DOESNT_END_WITH, CONTAINS, DOESNT_CONTAIN, MATCHES, NOT_MATCHES, EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESSER_THAN, LESSER_THAN_OR_EQUALS;
	}

}

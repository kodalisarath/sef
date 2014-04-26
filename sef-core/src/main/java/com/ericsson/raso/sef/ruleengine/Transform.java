package com.ericsson.raso.sef.ruleengine;

import java.io.Serializable;
import java.util.List;

/**
 * @author esatnar
 * 
 *         This is a marker interface for the rule engine semantics. The custom
 *         rule engine is designed over ECA (Event-Condition-Action)
 *         Architectural Pattern.
 * 
 */

public abstract class Transform implements Serializable {
	private static final long serialVersionUID = -631566732898601767L;

	private TransformType type = null;
	private List<String> values;

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public abstract Object apply(Object operand) throws TransformFailedException;

	public abstract int hashCode();

	public abstract boolean equals(Object obj);

	public abstract String toString();

	public TransformType getType() {
		return type;
	}

	public void setType(TransformType type) {
		this.type = type;
	}

	public enum TransformType {
		PREFIX, SUFFIX, REPLACE, SET_RESET, ADD, SUBTRACT, DIVIDE, MULTIPLY, MODULO, PERCENTILE_RAISE, PERCENTILE_REDUCE, PERCENTILE_VALUE, EXTENDED;
	}

}

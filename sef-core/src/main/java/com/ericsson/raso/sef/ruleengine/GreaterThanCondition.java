package com.ericsson.raso.sef.ruleengine;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public final class GreaterThanCondition extends Condition {
	private static final long serialVersionUID = -4000090910862060854L;

	// TODO: add logger initialization
	private Object refValue = null;

	public GreaterThanCondition(Object ruleValue) {

		if (ruleValue instanceof String) {
			// TODO: logger.debug("Rule Value: '" + ruleValue +
			// "' is of type 'String' and hence cannot be processed!!");
			throw new IllegalArgumentException("Expected 'Numeric' or 'Date' value. Found 'String'");
		}

		if (ruleValue instanceof Boolean) {
			// TODO: logger.debug("Rule Value: '" + ruleValue +
			// "' is of type 'String' and hence cannot be processed!!");
			throw new IllegalArgumentException("Expected 'Numeric' or 'Date' value. Found 'Boolean'");
		}

		if (ruleValue instanceof Number || ruleValue instanceof Date || ruleValue instanceof Calendar
				|| ruleValue instanceof Comparable) {
			// TODO: logger.debug("Rule Value: '" + ruleValue +
			// "' is either Numeric or Date or implements Comparable. Can process!!");
			this.setType(ConditionType.GREATER_THAN);
			this.refValue = ruleValue;
			setValues(Arrays.asList(new String[] { ruleValue.toString() }));
		}

	}

	@Override
	public boolean evaluate(Object operand) throws RuleFailedException {

		if (operand instanceof String) {
			// TODO: logger.debug("Operand: '" + operand +
			// "' is of type 'String' and hence cannot be processed!!");
			throw new RuleFailedException("Operand: " + operand + " is of type String and hence cannot apply Rule"
					+ this.toString());
		}

		if (operand instanceof Boolean) {
			// TODO: logger.debug("Operand: '" + operand +
			// "' is of type 'String' and hence cannot be processed!!");
			throw new RuleFailedException("Operand: " + operand + " is of type Boolean and hence cannot apply Rule"
					+ this.toString());
		}

		if (operand instanceof Number && this.refValue instanceof Number) {
			// polarity of the semantic condition is reversed to allow
			// definition of Rules from GUI meaningful rather than
			// the stack optimized implementation.
			if (((Number) this.refValue).doubleValue() < ((Number) operand).doubleValue()) {
				// TODO: logger.debug("Rule Value: '" + this.refValue +
				// "' is greater Operand: '" + operand);
				return true;
			} else {
				// TODO: logger.debug("Rule Value: '" + this.refValue +
				// "' is not greater Operand: '" + operand);
				return false;
			}
		}

		if (operand instanceof Comparable && this.refValue instanceof Comparable) {
			// polarity of the semantic condition is reversed to allow
			// definition of Rules from GUI meaningful rather than
			// the stack optimized implementation.
			if (((Comparable) this.refValue).compareTo(operand) < 0) {
				// TODO: logger.debug("Operand: '" + operand +
				// "' is greater Rule Value: '" + this.refValue);
				return true;
			} else {
				// TODO: logger.debug("Operand: '" + operand +
				// "' is not greater Rule Value: '" + this.refValue);
				return false;
			}
		}

		// TODO: logger.debug("FAIL:: Unknown types!! Rule Value: '" +
		// this.refValue + "' of type: " +
		// this.refValue.getClass().getCanonicalName() + " & Operand: '" +
		// operand + " of type: " + operand.getClass().getCanonicalName());
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((refValue == null) ? 0 : refValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GreaterThanCondition other = (GreaterThanCondition) obj;
		if (refValue == null) {
			if (other.refValue != null)
				return false;
		} else if (!refValue.equals(other.refValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return " is Greater Than '" + refValue + "'";
	}

}

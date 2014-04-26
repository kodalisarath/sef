package com.ericsson.raso.sef.ruleengine;

import java.util.Arrays;

public final class EqualsCondition extends Condition {
	private static final long serialVersionUID = -4629019892718572816L;

	// TODO: add logger initialization

	private Object value = null;

	public EqualsCondition(Object value) {
		if (value == null) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' argument for Rule");
		}

		super.setType(ConditionType.EQUALS);
		this.value = value;
		setValues(Arrays.asList(new String[] { value.toString() }));
	}

	@Override
	public boolean evaluate(Object operand) throws RuleFailedException {

		// Sanity Check
		if (operand == null) {
			// TODO:
			// logger.warn("Operand value is null/ empty and cannot be processed.");
			throw new RuleFailedException("Operand value is null/ empty and cannot be processed.");
		}

		// Evaluate the Rule
		if (this.value instanceof String && operand instanceof String) {
			String secondOperand = (String) operand;
			if (secondOperand.length() == 0) {
				// TODO:
				// logger.warn("Operand value is null/ empty and cannot be processed.");
				throw new RuleFailedException("Operand value is null/ empty and cannot be processed.");
			}

			if (!this.value.toString().equalsIgnoreCase(secondOperand)) {
				// TODO: logger.debug("Operand1: '" + this.value +
				// "' and Operand2'" + operand + "' are not equal".");
				return false;
			}
			return true;
		}
		
		
		if (this.value instanceof Comparable && operand instanceof Comparable) {
			if (((Comparable) this.value).compareTo(operand) == 0) {
				// TODO: logger.debug("Rule Value: '" + this.refValue +
				// "' is equals Operand: '" + operand);
				return true;
			} else {
				// TODO: logger.debug("Rule Value: '" + this.refValue +
				// "' is not equals Operand: '" + operand);
				return false;
			}
		} else {
			if (!this.value.equals(operand)) {
				// TODO: logger.debug("Operand1: '" + this.value +
				// "' and Operand2: '" + operand + "' are not equal.");
				return false;
			} else {
				// TODO: logger.debug("SUCCESS:: Operand1: '" + this.value +
				// "' and Operand2: '" + operand + "' are equal.");
				return true;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		EqualsCondition other = (EqualsCondition) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return " is Equals '" + value + "'";
	}

}

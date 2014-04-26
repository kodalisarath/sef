package com.ericsson.raso.sef.ruleengine;

import java.util.List;

public final class ExclusionCondition extends Condition {
	private static final long serialVersionUID = -3793915377985740159L;

	// TODO: add logger initialization

	private List<String> enumeratedList = null;

	public ExclusionCondition(List<String> enumeratedValues) {

		if (enumeratedValues == null) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' argument for Rule");
		}

		super.setType(ConditionType.EXCLUSION_LIST);
		this.enumeratedList = enumeratedValues;
		setValues(enumeratedValues);
	}

	@Override
	public boolean evaluate(Object operand) throws RuleFailedException {

		// Sanity Check
		if (operand == null) {
			// TODO:
			// logger.error("Operand value is null/ empty and cannot be processed.");
			throw new RuleFailedException("Operand value is null/ empty and cannot be processed.");
		}

		// Evaluate the Rule
		if (this.enumeratedList.contains(operand)) {
			// TODO: logger.warn("Value: " + parameterValue +
			// " should not be present in this rule's enumerated values.");
			return false;
		}
		// TODO: logger.debug("SUCCESS:: Value: " + parameterValue +
		// " is not present in this rule's enumerated values." +
		// this.enumeratedList);
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enumeratedList == null) ? 0 : enumeratedList.hashCode());
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
		ExclusionCondition other = (ExclusionCondition) obj;
		if (enumeratedList == null) {
			if (other.enumeratedList != null)
				return false;
		} else if (!enumeratedList.equals(other.enumeratedList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return " is Not Enumerated in '" + enumeratedList + "'";
	}
}

package com.ericsson.raso.sef.ruleengine;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DoesNotEndWithCondition extends Condition {
	private static final long serialVersionUID = 8014288212462133559L;
	// TODO: add logger initialization

	private Pattern endsWithPattern = null;

	public DoesNotEndWithCondition(String pattern) {
		if (pattern == null || !(pattern instanceof String) || pattern.length() == 0) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' or invalid type argument for Rule");
		}

		super.setType(ConditionType.DOESNT_END_WITH);
		this.endsWithPattern = Pattern.compile(pattern + ".*$");
		setValues(Arrays.asList(new String[] { pattern }));
	}

	@Override
	public boolean evaluate(Object operand) throws RuleFailedException {

		// Sanity Check
		if (operand == null) {
			// TODO:
			// logger.error("Operand value is null/ empty and cannot be processed.");
			throw new RuleFailedException("Operand value is null and cannot be processed.");
		}

		if (!(operand instanceof String)) {
			// TODO:
			// logger.error("Operand value is not 'String' Type and cannot be processed.");
			throw new RuleFailedException("Operand value is not 'String' Type and cannot be processed.");
		}

		// Evaluate the Rule
		String stringOperand = (String) operand;
		Matcher matcher = this.endsWithPattern.matcher(stringOperand);
		if (matcher.matches()) {
			// TODO: logger.warn("Value: " + operand +
			// " should not end with the pattern:" + this.pattern);
			return false;
		}
		// TODO: logger.debug("SUCCESS:: Value: " + parameterValue +
		// " does not end with the pattern:" + this.pattern);
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endsWithPattern == null) ? 0 : endsWithPattern.hashCode());
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
		DoesNotEndWithCondition other = (DoesNotEndWithCondition) obj;
		if (endsWithPattern == null) {
			if (other.endsWithPattern != null)
				return false;
		} else if (!endsWithPattern.equals(other.endsWithPattern))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return " Does Not End With '" + endsWithPattern + "'";
	}

}

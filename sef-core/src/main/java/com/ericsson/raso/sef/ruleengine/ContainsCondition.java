package com.ericsson.raso.sef.ruleengine;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ContainsCondition extends Condition {
	private static final long serialVersionUID = 4625515439867229384L;
	// TODO: add logger initialization

	private Pattern pattern = null;

	public ContainsCondition(String pattern) {
		if (pattern == null || !(pattern instanceof String) || pattern.length() == 0) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' or invalid type argument for Rule");
		}

		super.setType(ConditionType.CONTAINS);
		this.pattern = Pattern.compile("^.*" + pattern + ".*$");
		setValues(Arrays.asList(new String[] { pattern }));
	}

	@Override
	public boolean evaluate(Object operand) throws RuleFailedException {
		
		String stringOperand = operand.toString();
		
		if (stringOperand == null || stringOperand.length() == 0) {
			// TODO:
			// logger.error("Operand value is null/ empty and cannot be processed.");
			throw new RuleFailedException("Operand value is null/ empty and cannot be processed.");
		}

		// Evaluate the Rule
		Matcher matcher = this.pattern.matcher(stringOperand);
		if (!matcher.matches()) {
			// TODO: logger.info("Value: " + operand +
			// " should contain the pattern:" + this.pattern);
			return false;
		}
		// TODO: logger.debug("SUCCESS:: Value: " + parameterValue +
		// " contains the pattern:" + this.pattern);
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
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
		ContainsCondition other = (ContainsCondition) obj;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return " Contains '" + pattern + "'";
	}

}

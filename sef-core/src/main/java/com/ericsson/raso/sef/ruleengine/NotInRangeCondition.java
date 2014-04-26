package com.ericsson.raso.sef.ruleengine;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public final class NotInRangeCondition extends Condition {
	private static final long serialVersionUID = -7410131652255190338L;

	// TODO: add logger initialization
	private Object lowerBound = null;
	private Object upperBound = null;

	public NotInRangeCondition(Object lowerBound, Object upperBound) {
		super();

		if (!(lowerBound != null && (lowerBound instanceof Number || lowerBound instanceof Date || lowerBound instanceof Calendar))) {
			// TODO:
			// logger.debug("Expected Numeric or Date type for lowerBound. Found: "
			// + lowerBound + "!!");
			throw new IllegalArgumentException("Expected Numeric or Date type for lowerBound. Found: " + lowerBound
					+ "!!");
		}

		if (!(upperBound != null && (upperBound instanceof Number || upperBound instanceof Date || upperBound instanceof Calendar))) {
			// TODO:
			// logger.debug("Expected Numeric or Date type for lowerBound. Found: "
			// + lowerBound + "!!");
			throw new IllegalArgumentException("Expected Numeric or Date type for lowerBound. Found: " + lowerBound
					+ "!!");
		}

		if (((Comparable) lowerBound).compareTo(upperBound) >= 0) {
			// TODO: logger.debug("lowerBound: " + lowerBound +
			// " is not lower than upperBound: " + upperBound + "!!");
			throw new IllegalArgumentException("lowerBound: " + lowerBound + " is not lower than upperBound: "
					+ upperBound + "!!");
		}

		super.setType(ConditionType.NOT_IN_RANGE);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		setValues(Arrays.asList(new String[] { lowerBound.toString(), upperBound.toString() }));
	}

	@Override
	public boolean evaluate(Object operand) throws RuleFailedException {

		if ((operand != null && (operand instanceof CharSequence || operand instanceof String))) {
			// TODO:
			// logger.debug("Expected Numeric or Date type for lowerBound. Found: "
			// + lowerBound + "!!");
			throw new IllegalArgumentException("Expected Numeric or Date type for operand. Found: " + operand + "!!");
		}

		if (!(operand != null && (operand instanceof Number || operand instanceof Date || operand instanceof Calendar))) {
			// TODO:
			// logger.debug("Expected Numeric or Date type for lowerBound. Found: "
			// + lowerBound + "!!");
			throw new IllegalArgumentException("Expected Numeric or Date type for operand. Found: " + operand + "!!");
		}

		if ((((Comparable) this.lowerBound).compareTo(operand) <= 0)
				&& (((Comparable) this.upperBound).compareTo(operand) >= 0)) {
			// TODO: logger.debug("Operand : " + operand + " is within Range: ["
			// + lowerBound + " --> " + upperBound + "]");
			return false;
		} else {
			// TODO: logger.debug("Operand : " + operand +
			// " is not within Range: [" + lowerBound + " --> " + upperBound +
			// "]");
			return true;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lowerBound == null) ? 0 : lowerBound.hashCode());
		result = prime * result + ((upperBound == null) ? 0 : upperBound.hashCode());
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
		NotInRangeCondition other = (NotInRangeCondition) obj;
		if (lowerBound == null) {
			if (other.lowerBound != null)
				return false;
		} else if (!lowerBound.equals(other.lowerBound))
			return false;
		if (upperBound == null) {
			if (other.upperBound != null)
				return false;
		} else if (!upperBound.equals(other.upperBound))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return " is Not in Range between '" + lowerBound + "' and '" + upperBound + "'";
	}

}

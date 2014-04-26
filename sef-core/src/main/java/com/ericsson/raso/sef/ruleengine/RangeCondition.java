package com.ericsson.raso.sef.ruleengine;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A Range Condition for testing range between comparable objects.
 */
public final class RangeCondition extends Condition {

	/** The constant serialVersionUID */
	private static final long serialVersionUID = -2847852304909338933L;

	/** The reference lowerbound */
	private Object lowerBound = null;

	/** The reference upperbound */
	private Object upperBound = null;

	/** The CONSTANT logger */
	//TODO: private transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	/**
	 * Create a constructor.
	 * 
	 * @param lowerBound
	 *            the values
	 * @param upperBound
	 *            the data type
	 * 
	 */
	public RangeCondition(final Object lowerBound, final Object upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		super.setType(ConditionType.RANGE);
		setValues(Arrays.asList(new String[] { lowerBound.toString(), upperBound.toString() }));
	}

//	/**
//	 * Logger object required for Logging.
//	 * 
//	 * @return logger
//	 */
//	protected org.apache.log4j.Logger getLogger() {
//		if (logger == null) {
//			logger = org.apache.log4j.Logger.getLogger(this.getClass());
//		}
//		return logger;
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean evaluate(Object operand) throws RuleFailedException {

		if ((operand != null && (operand instanceof CharSequence || operand instanceof String))) {
			// TODO:
			// logger.debug("Expected Numeric or Date type for lowerBound. Found: "
			// + lowerBound + "!!");
			throw new IllegalArgumentException("Expected Numeric or Date type for operand. Found: " + operand + "!!");
		}

		
		if (!(operand != null && (operand instanceof Number || operand instanceof Date || operand instanceof Calendar || operand instanceof InetAddress))) {
			// TODO:
			// logger.debug("Expected Numeric or Date type or IP Address for lowerBound. Found: "
			// + lowerBound + "!!");
			throw new IllegalArgumentException("Expected Numeric or Date type for operand. Found: " + operand + "!!");
		}
		
		if (operand instanceof InetAddress) {
			long lOperand = ipToLong((InetAddress) operand);
			long lLowerBound = ipToLong((InetAddress) this.lowerBound);
			long lUpperBound = ipToLong((InetAddress) this.upperBound);
			
			//TODO: getLogger().info(
			//		"RangeCondition Operand value is of Type IP Address and its values are : lower "
			//				+ lLowerBound + ", operand " + lOperand + " ,upper " + lUpperBound);

			if ( lLowerBound <= lOperand && lOperand <= lUpperBound) {
				return true;
			} else {
				return false;
			}
		}

		if (operand instanceof Comparable && lowerBound instanceof Comparable && upperBound instanceof Comparable) {
			// fromDate <= todayDate <= toDate
			if ((((Comparable) this.lowerBound).compareTo(operand) <= 0)
					&& (((Comparable) this.upperBound).compareTo(operand) >= 0)) {
			//TODO:	getLogger().info(
			//			"RangeCondition Operand value implements Comparable and its values are : lower "
			//					+ lowerBound + ", operand " + operand + " ,upper " + upperBound);
				return true;
			} else {
				return false;
			}
		} else {
			//TODO: getLogger().error(
			//		"RangeCondition  not a valid Type lowerBound " + lowerBound + ", operand " + operand
			//				+ " ,upperBound " + upperBound);

		}

		return false;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RangeCondition other = (RangeCondition) obj;
		if (lowerBound == null) {
			if (other.lowerBound != null) {
				return false;
			}
		} else if (!lowerBound.equals(other.lowerBound)) {
			return false;
		}
		if (upperBound == null) {
			if (other.upperBound != null) {
				return false;
			}
		} else if (!upperBound.equals(other.upperBound)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RangeCondition [object1=" + lowerBound + ", object2=" + upperBound + "]";
	}

}

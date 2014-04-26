/*
 * Project:       DU CCBS
 *
 * $Revision: 6653 $, last modified $Date:$ by $Author: Sahil $
 */
package com.ericsson.raso.sef.ruleengine;

import java.util.List;

/**
 * The Class EnumeratedCondition.
 */
public final class EnumeratedCondition extends Condition {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2026010127046321896L;

	// TODO: add logger initialization

	/** The enumerated list. */
	private List<String> enumeratedList = null;

	/** The CONSTANT logger. */
	//TODO: private transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	/**
	 * Instantiates a new enumerated condition.
	 * 
	 * @param enumeratedValues
	 *            the enumerated values
	 */
	public EnumeratedCondition(List<String> enumeratedValues) {
		if (enumeratedValues == null) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' argument for Rule");
		}

		super.setType(ConditionType.ENUMERATED);
		this.enumeratedList = enumeratedValues;
		setValues(enumeratedValues);
	}

//	/**
//	 * Logger object required for Logging.
//	 * 
//	 * @return logger
//	 */
//	private org.apache.log4j.Logger getLogger() {
//		if (logger == null) {
//			logger = org.apache.log4j.Logger.getLogger(this.getClass());
//		}
//		return logger;
//	}

	@Override
	public boolean evaluate(Object operand) throws RuleFailedException {

		// Sanity Check
		if (operand == null) {
			// TODO:
			// logger.error("Operand value is null/ empty and cannot be processed.");
			throw new RuleFailedException("Operand value is null/ empty and cannot be processed.");
		}

		if (operand instanceof List) {
			List<String> operandList = ((List) operand);
			//TODO: getLogger().info("EnumeratedCondition : eligibleList is   " + operandList);
			if (enumeratedList.size() > 0) {
				boolean eligible = operandList.contains(enumeratedList.get(0));
				//TODO: getLogger().info("EnumeratedCondition : eligible for purchase or not  " + eligible);
				return eligible;
			} else {
				return false;
			}
		}
		// Evaluate the Rule
		if (!this.enumeratedList.contains(operand)) {
			// TODO: logger.warn("Value: " + parameterValue +
			// " should be present in this rule's enumerated values." +
			// this.enumeratedList);
			return false;
		}
		// TODO: logger.debug("SUCCESS:: Value: " + parameterValue +
		// " is present in this rule's enumerated values." +
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EnumeratedCondition other = (EnumeratedCondition) obj;
		if (enumeratedList == null) {
			if (other.enumeratedList != null) {
				return false;
			}
		} else if (!enumeratedList.equals(other.enumeratedList)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return " is Enumerated in '" + enumeratedList + "'";
	}

}

package com.ericsson.raso.sef.ruleengine;

import java.util.Arrays;

public final class ResetTransform extends Transform {
	private static final long serialVersionUID = 8843435290451632347L;

	// TODO: add logger initialization

	private Object resetValue = null;

	public ResetTransform(Object resetValue) {
		if (!(resetValue != null)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' or invalid type argument for Transform");
		}

		super.setType(TransformType.SET_RESET);
		this.resetValue = resetValue;
		setValues(Arrays.asList(new String[] { resetValue.toString() }));
	}

	@Override
	public Object apply(Object operand) throws TransformFailedException {

		// sanity check
		if (operand == null) {
			// TODO: logger.debug("Context param: " + attribute +
			// " was not found. Creating a new attribute...");
		}

		// perform transform
		return resetValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resetValue == null) ? 0 : resetValue.hashCode());
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
		ResetTransform other = (ResetTransform) obj;
		if (resetValue == null) {
			if (other.resetValue != null)
				return false;
		} else if (!resetValue.equals(other.resetValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Set/Reset [absoluteValue '" + resetValue + "']";
	}

}

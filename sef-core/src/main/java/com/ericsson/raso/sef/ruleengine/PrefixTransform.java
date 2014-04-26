package com.ericsson.raso.sef.ruleengine;

import java.util.Arrays;

public final class PrefixTransform extends Transform {
	private static final long serialVersionUID = 724158005531529662L;

	// TODO: add logger initialization

	private String prefix = null;

	public PrefixTransform(Object prefix) {
		if (!(prefix != null && prefix instanceof String && ((String) prefix).length() > 0)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' or invalid type argument for Transform");
		}

		super.setType(TransformType.PREFIX);
		this.prefix = (String) prefix;
		setValues(Arrays.asList(new String[] { prefix.toString() }));
	}

	@Override
	public Object apply(Object operand) throws TransformFailedException {

		// sanity check
		if (!(operand != null && operand instanceof String && ((String) prefix).length() > 0)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new TransformFailedException("Found 'null' or invalid type argument");
		}

		// perform transform
		return (prefix + operand);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
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
		PrefixTransform other = (PrefixTransform) obj;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return " Prefix [token '" + prefix + "']";
	}

}

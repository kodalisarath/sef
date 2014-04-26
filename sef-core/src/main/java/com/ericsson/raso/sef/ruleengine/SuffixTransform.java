package com.ericsson.raso.sef.ruleengine;

import java.util.Arrays;

public final class SuffixTransform extends Transform {
	private static final long serialVersionUID = -6356655756564744144L;

	// TODO: add logger initialization

	private String suffix = null;

	public SuffixTransform(Object suffix) {
		if (!(suffix != null && suffix instanceof String && ((String) suffix).length() > 0)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' or invalid type argument for Transform");
		}

		super.setType(TransformType.SUFFIX);
		this.suffix = (String) suffix;
		setValues(Arrays.asList(new String[] { suffix.toString() }));
	}

	@Override
	public Object apply(Object operand) throws TransformFailedException {

		// sanity check
		if (!(operand != null && operand instanceof String && ((String) operand).length() > 0)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new TransformFailedException("Found 'null' or invalid type argument");
		}

		// perform transform
		return (operand + suffix);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
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
		SuffixTransform other = (SuffixTransform) obj;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Suffix [token '" + suffix + "']";
	}

}

package com.ericsson.raso.sef.ruleengine;

import java.util.Arrays;

public final class ReplaceTransform extends Transform {
	private static final long serialVersionUID = 8547933916657653650L;

	// TODO: add logger initialization

	private String search = null;
	private String replace = null;
	private boolean isGreedy = false;

	public ReplaceTransform(String search, String replace, boolean isGreedy) {
		super.setType(TransformType.SUFFIX);
		this.search = (String) search;
		this.replace = (String) replace;
		this.isGreedy = isGreedy;
		setValues(Arrays.asList(new String[] { search, replace }));
	}

	@Override
	public Object apply(Object operand) throws TransformFailedException {

		// sanity check
		if (!(operand != null && operand instanceof String && ((String) search).length() > 0)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new TransformFailedException("Found 'null' or invalid type argument");
		}

		// perform transform
		return (isGreedy) ? ((String) operand).replaceAll(search, replace) : ((String) operand).replaceFirst(search,
				replace);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isGreedy ? 1231 : 1237);
		result = prime * result + ((replace == null) ? 0 : replace.hashCode());
		result = prime * result + ((search == null) ? 0 : search.hashCode());
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
		ReplaceTransform other = (ReplaceTransform) obj;
		if (isGreedy != other.isGreedy)
			return false;
		if (replace == null) {
			if (other.replace != null)
				return false;
		} else if (!replace.equals(other.replace))
			return false;
		if (search == null) {
			if (other.search != null)
				return false;
		} else if (!search.equals(other.search))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Replace [searchFor '" + search + "' and replaceWith '" + replace + "' forAllOccurance '" + isGreedy
				+ "']";
	}

}

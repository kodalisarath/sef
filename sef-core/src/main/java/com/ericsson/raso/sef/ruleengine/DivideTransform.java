package com.ericsson.raso.sef.ruleengine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public final class DivideTransform extends Transform {
	private static final long serialVersionUID = 8076025013399376607L;

	// TODO: add logger initialization

	private Number offset = null;

	public DivideTransform(Number offset) {
		if (!(offset != null && offset instanceof Number)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' or invalid type argument '" + offset + "' for Transform");
		}

		super.setType(TransformType.DIVIDE);
		this.offset = (Number) offset;
		setValues(Arrays.asList(new String[] { offset.toString() }));
	}

	@Override
	public Object apply(Object operand) throws TransformFailedException {
		// sanity check
		if (!(operand != null && operand instanceof Number)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new TransformFailedException("Found 'null' or invalid type argument '" + operand);
		}

		// perform transform
		if (operand instanceof Byte) {
			return ((Byte) operand) / this.offset.byteValue();
		}

		if (operand instanceof Double) {
			return ((Double) operand) / this.offset.doubleValue();
		}

		if (operand instanceof Float) {
			return ((Float) operand) / this.offset.floatValue();
		}

		if (operand instanceof Long) {
			return ((Long) operand) / this.offset.longValue();
		}

		if (operand instanceof Short) {
			return ((Short) operand) / this.offset.shortValue();
		}

		if (operand instanceof Integer) {
			return ((Integer) operand) / this.offset.intValue();
		}

		if (operand instanceof BigInteger) {
			if (this.offset instanceof BigInteger) {
				return ((BigInteger) operand).divide((BigInteger) this.offset);
			}

			if (this.offset instanceof BigDecimal) {
				return ((BigDecimal) operand).divide((BigDecimal) this.offset);
			} else {
				// TODO:
				// logger.debug("FAIL:: Incompatible types!! Rule Value: '" +
				// this.offset + "' of type: " +
				// this.offset.getClass().getCanonicalName() +
				// " & Context param: '" + param + " of type: " +
				// param.getClass().getCanonicalName());
				throw new TransformFailedException("Incompatible types!!");
			}
		}

		// TODO: logger.debug("FAIL:: Unknown types!! Rule Value: '" +
		// this.offset + "' of type: " +
		// this.offset.getClass().getCanonicalName() + " & Context param: '" +
		// param + " of type: " + param.getClass().getCanonicalName());
		throw new TransformFailedException("Unknown types!!");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offset == null) ? 0 : offset.hashCode());
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
		DivideTransform other = (DivideTransform) obj;
		if (offset == null) {
			if (other.offset != null)
				return false;
		} else if (!offset.equals(other.offset))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Divide [denominator '" + offset + "']";
	}

}

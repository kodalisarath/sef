package com.ericsson.raso.sef.ruleengine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public final class PercentileValueTransform extends Transform {
	private static final long serialVersionUID = 2091932413936510493L;

	// TODO: add logger initialization

	private Number percentile = null;

	public PercentileValueTransform(Number percentile) {
		if (!(percentile != null && percentile instanceof Number)) {
			// TODO: logger.debug("Rule Value: is null!!");
			throw new IllegalArgumentException("Found 'null' or invalid type argument '" + percentile
					+ "' for Transform");
		}

		super.setType(TransformType.PERCENTILE_VALUE);
		this.percentile = ((Number) percentile);
		setValues(Arrays.asList(new String[] { percentile.toString() }));
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
			return ((Byte) operand) * ((Byte) this.percentile) / 100;
		}

		if (operand instanceof Double) {
			return ((Double) operand) * ((Double) this.percentile) / 100;
		}

		if (operand instanceof Float) {
			return ((Float) operand) * ((Float) this.percentile) / 100;
		}

		if (operand instanceof Long) {
			return ((Long) operand) * ((Long) this.percentile) / 100;
		}

		if (operand instanceof Short) {
			return ((Short) operand) * ((Short) this.percentile) / 100;
		}

		if (operand instanceof Integer) {
			return ((Integer) operand) * ((Integer) this.percentile) / 100;
		}

		if (operand instanceof BigInteger) {
			if (this.percentile instanceof BigInteger) {
				return ((BigInteger) operand).multiply((BigInteger) this.percentile).divide(BigInteger.valueOf(100));
			}

			if (this.percentile instanceof BigDecimal) {
				return ((BigDecimal) operand).multiply((BigDecimal) this.percentile).divide(BigDecimal.valueOf(100));
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
		result = prime * result + ((percentile == null) ? 0 : percentile.hashCode());
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
		PercentileValueTransform other = (PercentileValueTransform) obj;
		if (percentile == null) {
			if (other.percentile != null)
				return false;
		} else if (!percentile.equals(other.percentile))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Percentile Value [percentage '" + percentile + "']";
	}

}

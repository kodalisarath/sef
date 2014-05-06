package com.ericsson.raso.sef.ruleengine;

import java.io.Serializable;

import com.ericsson.raso.sef.core.FetchRequestContextTask;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.ReqContext;


public final class TransformUnit implements Serializable {
	private static final long serialVersionUID = -2668027884595306628L;

	private String transformUnitName = null;
	private ExternDataUnitTask<?> inputData = null;
	private Transform transform = null;
	private String outputSchema = null;
	private String baseObjectName;
	private String transformVariable;

	public TransformUnit(ExternDataUnitTask<?> inputData, Transform transform, String outputSchema) {
		if (inputData == null || transform == null) {
			throw new IllegalArgumentException("Found 'null' arguments for inputData or transform");
		}

		if (!(outputSchema != null && outputSchema.length() > 0)) {
			throw new IllegalArgumentException("Found 'null'or empty argument for outputSchema");
		}

		this.inputData = inputData;
		this.transform = transform;
		this.outputSchema = outputSchema;
	}

	public boolean execute() throws TransformFailedException {
		Object operand = null;
		try {
			operand = this.inputData.execute();
		} catch (FrameworkException e) {
			return false;
		}

		Object result = null;
		try {
			result = this.transform.apply(operand);
			ReqContext requestContext = new FetchRequestContextTask().execute();
			requestContext.putTransient(transformVariable, result);
		} catch (TransformFailedException e) {
			return false;
		} catch (FrameworkException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public TransformUnit(String transformUnitName, ExternDataUnitTask<?> inputData, Transform transform,
			String baseObjectName, String transformVariable, String outputSchema) {
		this(inputData, transform, outputSchema);
		this.baseObjectName = baseObjectName;
		this.transformVariable = transformVariable;
		this.transformUnitName = transformUnitName;
	}

	@Override
	public String toString() {
		return "TransformUnit [transformUnitName=" + transformUnitName + ", inputData=" + inputData + ", transform="
				+ transform + ", outputSchema=" + outputSchema + ", baseObjectName=" + baseObjectName
				+ ", transformVariable=" + transformVariable + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseObjectName == null) ? 0 : baseObjectName.hashCode());
		result = prime * result + ((inputData == null) ? 0 : inputData.hashCode());
		result = prime * result + ((outputSchema == null) ? 0 : outputSchema.hashCode());
		result = prime * result + ((transform == null) ? 0 : transform.hashCode());
		result = prime * result + ((transformUnitName == null) ? 0 : transformUnitName.hashCode());
		result = prime * result + ((transformVariable == null) ? 0 : transformVariable.hashCode());
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
		TransformUnit other = (TransformUnit) obj;
		if (baseObjectName == null) {
			if (other.baseObjectName != null)
				return false;
		} else if (!baseObjectName.equals(other.baseObjectName))
			return false;
		if (inputData == null) {
			if (other.inputData != null)
				return false;
		} else if (!inputData.equals(other.inputData))
			return false;
		if (outputSchema == null) {
			if (other.outputSchema != null)
				return false;
		} else if (!outputSchema.equals(other.outputSchema))
			return false;
		if (transform == null) {
			if (other.transform != null)
				return false;
		} else if (!transform.equals(other.transform))
			return false;
		if (transformUnitName == null) {
			if (other.transformUnitName != null)
				return false;
		} else if (!transformUnitName.equals(other.transformUnitName))
			return false;
		if (transformVariable == null) {
			if (other.transformVariable != null)
				return false;
		} else if (!transformVariable.equals(other.transformVariable))
			return false;
		return true;
	}

	public String getTransformUnitName() {
		return transformUnitName;
	}

	public void setTransformUnitName(String transformUnitName) {
		this.transformUnitName = transformUnitName;
	}

	public String getTransformVariable() {
		return transformVariable;
	}

	public void setTransformVariable(String transformVariable) {
		this.transformVariable = transformVariable;
	}

	public String getBaseObjectName() {
		return baseObjectName;
	}

	public void setBaseObjectName(String baseObjectName) {
		this.baseObjectName = baseObjectName;
	}

	public String getOutputSchema() {
		return outputSchema;
	}

	public void setOutputSchema(String outputSchema) {
		this.outputSchema = outputSchema;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

}

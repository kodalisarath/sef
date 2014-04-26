package com.ericsson.raso.sef.ruleengine;

import java.io.Serializable;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Task;


public abstract class ExternDataUnitTask<T> implements Task<T>, Serializable  {

	private static final long serialVersionUID = 1L;

	@Override
	public abstract T execute() throws FrameworkException;

	public abstract int hashCode();

	public abstract boolean equals(Object theOtherOne);

	public abstract String toString();
}

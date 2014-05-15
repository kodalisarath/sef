package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;

public abstract class AbstractResponse implements Serializable {
	private static final long serialVersionUID = 6889864854694969170L;

	private String requestCorrelator = null;
	private Map<String, Object> atomicStepResults = null;
	private TransactionException returnFault = null;

	public AbstractResponse(String requestCorrelator) {
		super();
		this.requestCorrelator = requestCorrelator;
	}

	public String getRequestCorrelator() {
		return requestCorrelator;
	}

	public void setRequestCorrelator(String requestCorrelator) {
		this.requestCorrelator = requestCorrelator;
	}
	
	
	public void addAtomicStepResult(String step, Object intermediaryResult) {
		if (this.atomicStepResults == null)
			this.atomicStepResults = new TreeMap<String, Object>();
		
		this.atomicStepResults.put(step, intermediaryResult);
	}

	public Map<String, Object> getAtomicStepResults() {
		return atomicStepResults;
	}

	public void setAtomicStepResults(Map<String, Object> atomicStepResults) {
		this.atomicStepResults = atomicStepResults;
	}

	public TransactionException getReturnFault() {
		return returnFault;
	}

	public void setReturnFault(TransactionException returnFault) {
		this.returnFault = returnFault;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestCorrelator == null) ? 0 : requestCorrelator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractResponse))
			return false;
		AbstractResponse other = (AbstractResponse) obj;
		if (requestCorrelator == null) {
			if (other.requestCorrelator != null)
				return false;
		} else if (!requestCorrelator.equals(other.requestCorrelator))
			return false;
		return true;
	}
	
	

}

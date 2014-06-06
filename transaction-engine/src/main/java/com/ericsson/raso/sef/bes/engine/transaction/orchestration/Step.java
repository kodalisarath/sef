package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;

public abstract class Step<Object> implements Serializable, Callable<Object>, Comparable<Step<Object>> {
	private static final long	serialVersionUID	= -4938200229157969953L;
	
	protected String stepCorrelator = null;
	private TransactionTask executionInputs = null;
	private Object result = null;
	private StepExecutionException fault = null;
	
	
	

	Step(String stepCorrelator, TransactionTask executionInputs) {
		super();
		this.stepCorrelator = stepCorrelator;
		this.executionInputs = executionInputs;
	}
	
	public abstract Object execute();
	

	@Override
	public Object call() throws Exception {
		return this.execute();
	}

	public String getStepCorrelator() {
		return stepCorrelator;
	}

	public TransactionTask getExecutionInputs() {
		return executionInputs;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public StepExecutionException getFault() {
		return fault;
	}

	public void setFault(StepExecutionException fault) {
		this.fault = fault;
	}
	
	public int compareTo(Step<Object> t) {
		return t.stepCorrelator.compareTo(this.stepCorrelator);
	}

}

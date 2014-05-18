package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;

public abstract class Step<T> implements Serializable, Callable<Object> {
	private static final long	serialVersionUID	= -4938200229157969953L;
	
	private String stepCorrelator = null;
	private TransactionTask executionInputs = null;
	private T result = null;
	private StepExecutionException fault = null;
	
	
	

	Step(String stepCorrelator, TransactionTask executionInputs) {
		super();
		this.stepCorrelator = stepCorrelator;
		this.executionInputs = executionInputs;
	}
	
	public abstract T execute();
	

	@Override
	public T call() throws Exception {
		return this.execute();
	}

	public String getStepCorrelator() {
		return stepCorrelator;
	}

	public TransactionTask getExecutionInputs() {
		return executionInputs;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public StepExecutionException getFault() {
		return fault;
	}

	public void setFault(StepExecutionException fault) {
		this.fault = fault;
	}
	
	

}

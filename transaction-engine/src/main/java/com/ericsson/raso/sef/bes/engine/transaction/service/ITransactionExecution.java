package com.ericsson.raso.sef.bes.engine.transaction.service;

public interface ITransactionExecution<T>  {
	
	public abstract void execute(T transaction);

}

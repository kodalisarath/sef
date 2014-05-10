package com.ericsson.raso.sef.core;

public interface Command<T> {
	
	T execute() throws SmException;

}

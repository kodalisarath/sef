package com.ericsson.raso.sef.core;

public interface Task<T> {
	
	T execute() throws FrameworkException, SmException;

}

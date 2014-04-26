package com.ericsson.raso.sef.core;

public interface RequestContext {
	
	<T> T get(String key);

	<T> void put(String key, T value);
	
	<T> void putTransient(String key, T value);

}

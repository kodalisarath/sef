package com.ericsson.raso.sef.core;

import java.util.concurrent.atomic.AtomicLong;

import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IFunction;

public class LocalAtomicLong extends AtomicLong implements IAtomicLong {
	private static final long serialVersionUID = 3708496953015479466L;
	private String name;
	
	

	public LocalAtomicLong(String name) {
		super();
		this.name = name;
	}

	@Override
	public Object getId() {
		return "NOPE";
	}

	@Override
	public String getPartitionKey() {
		return "NAH";
	}

	@Override
	public String getServiceName() {
		return "NANA";
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void alter(IFunction<Long, Long> function) {
		
	}

	@Override
	public long alterAndGet(IFunction<Long, Long> function) {
		return -1;
	}

	@Override
	public long getAndAlter(IFunction<Long, Long> function) {
		return -1;
	}

	@Override
	public <R> R apply(IFunction<Long, R> function) {
		return null;
	}

	

}

package com.ericsson.raso.sef.core;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.hazelcast.core.ISemaphore;

public class LocalSemaphore extends Semaphore implements ISemaphore {
	
	private static final long serialVersionUID = -9075446995903723065L;

	private String name;
	private ConcurrentHashMap<String, ISemaphore> store;
	
	
	public LocalSemaphore(String name) {
		super(0);
		this.name = name;
	}
	
	public LocalSemaphore(int permits) {
		super(permits);
	}

	public LocalSemaphore(int permits, boolean fair) {
		super(permits, fair);
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
		return "NAHAH";
	}

	@Override
	public void destroy() {
		this.store.remove(this.name);
	}

	public ConcurrentHashMap<String, ISemaphore> getStore() {
		return store;
	}

	public void setStore(ConcurrentHashMap<String, ISemaphore> store) {
		this.store = store;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean init(int permits) {
		super.release(permits);
		return true;
	}

	@Override
	public void acquire() throws InterruptedException {
		super.acquire();
	}

	@Override
	public void acquire(int permits) throws InterruptedException {
		super.acquire(permits);
	}

	@Override
	public int availablePermits() {
		return super.availablePermits();
	}

	@Override
	public int drainPermits() {
		return super.drainPermits();
	}

	@Override
	public void reducePermits(int reduction) {
		super.reducePermits(reduction);
	}

	@Override
	public void release() {
		super.release();
	}

	@Override
	public void release(int permits) {
		super.release(permits);
	}

	@Override
	public boolean tryAcquire() {
		return super.tryAcquire();
	}

	@Override
	public boolean tryAcquire(int permits) {
		return tryAcquire(permits);
	}

	@Override
	public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
		return super.tryAcquire(timeout, unit);
	}

	@Override
	public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException {
		return super.tryAcquire(permits, timeout, unit);
	}
	

}

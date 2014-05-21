package com.ericsson.raso.sef.core;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

public interface CloudAwareCluster {

	public void start();
	public void stop();
	
	public <K, V> Map<K, V> getMap(String name);
	
	public ExecutorService getDistributedService(String name) ;
	
	public IAtomicLong getAtomicCounter(String name);
	
	public Lock getLock(String key);
	
	public HazelcastInstance getInstance();
	
}

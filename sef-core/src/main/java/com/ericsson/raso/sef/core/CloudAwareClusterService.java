package com.ericsson.raso.sef.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

public class CloudAwareClusterService {

	private HazelcastInstance instance;
	private static final String CONFIG_HOME = "CONFIG_HOME";
	
	public CloudAwareClusterService() {
		
	}
	
	public void start() {
		String configHome = System.getenv(CONFIG_HOME);
		try {
			FileInputStream fis = new FileInputStream(configHome + "hazelcast.xml");
			Config config = new XmlConfigBuilder(fis).build();
			instance = Hazelcast.newHazelcastInstance(config);
			
		} catch (FileNotFoundException e) {
			//TODO: Throw SNMP Unable load cluster configuration. Cannot start
			e.printStackTrace();
			throw new RuntimeException("Cannot load cluster configuration");
		}
	}

	public void stop() {
		instance.shutdown();
	}
	
	public <K, V> Map<K, V> getMap(String name) {
		return instance.getMap(name);
	}
	
	public ExecutorService getDistributedService(String name) {
		return instance.getExecutorService(name);
	}
	
	public IAtomicLong getAtomicCounter(String name) {
		return instance.getAtomicLong(name);
	}
	
	public Lock getLock(String key) {
		return instance.getLock(key);
	}
	
	public HazelcastInstance getInstance() {
		return instance;
	}
}

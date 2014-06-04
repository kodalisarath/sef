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
import com.hazelcast.core.ISemaphore;

public class CloudAwareClusterService implements CloudAwareCluster {

	private HazelcastInstance instance;
	private static final String HZ_CONFIG = "HZ_CONFIG";
	
	public CloudAwareClusterService() {
		start();
	}
	
	public void start() {
		String configHome = System.getenv(HZ_CONFIG);
		try {
			FileInputStream fis = new FileInputStream(configHome + getFileSeparator() +  "hazelcast.xml");
			Config config = new XmlConfigBuilder(fis).build();
			instance = Hazelcast.newHazelcastInstance(config);
			
		} catch (FileNotFoundException e) {
			//TODO: Throw SNMP Unable load cluster configuration. Cannot start
			e.printStackTrace();
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
	
	public ISemaphore getSemaphore(String name) {
		ISemaphore controlSignal = instance.getSemaphore(name);
		return controlSignal;
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
	
	
	private String getFileSeparator() {
		
		String your_os = System.getProperty("os.name").toLowerCase();
		if(your_os.indexOf("win") >= 0){
			return "\\";
		}else if(your_os.indexOf( "nix") >=0 || your_os.indexOf( "nux") >=0){
			return "/";
		}else{
			return "/";
		}
	}
}

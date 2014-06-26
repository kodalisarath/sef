package com.ericsson.raso.sef.core;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.ISemaphore;

public class CloudAwareClusterService<K, V> implements CloudAwareCluster {

	//private HazelcastInstance instance;
	private CloudAwareClusterService<K, V> instance = new CloudAwareClusterService<K, V>();
	
	private static final String HZ_CONFIG = "HZ_CONFIG";
	
	private TreeMap<String, HashMap<K, V>> localStore = new TreeMap<String, HashMap<K,V>>(); 
	private TreeMap<String, ISemaphore> localSignals = new TreeMap<String, ISemaphore>(); 
	
	public CloudAwareClusterService() {
		start();
	}
	
	public void start() {
//		String configHome = System.getenv(HZ_CONFIG);
//		try {
//			FileInputStream fis = new FileInputStream(configHome + getFileSeparator() +  "hazelcast.xml");
//			Config config = new XmlConfigBuilder(fis).build();
////			// config.setClassLoader(getClass().getClassLoader());
//			instance = Hazelcast.newHazelcastInstance(config);
//			
//		} catch (FileNotFoundException e) {
//			//TODO: Throw SNMP Unable load cluster configuration. Cannot start
//			e.printStackTrace();
//		}
	}

	public void stop() {
//		instance.shutdown();
	}
	
	public Map<K, V> getMap(String name) {
		//return instance.getMap(name);
		
		HashMap<K, V> namedMap = this.localStore.get(name);
		if (namedMap == null) {
			namedMap = new HashMap<K,V>();
			this.localStore.put(name, namedMap);
		}
		return namedMap;
			
	}
	
	public ExecutorService getDistributedService(String name) {
		//return instance.getExecutorService(name);
		return new ThreadPoolExecutor(5, 40, 30000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
	}
	
	public ISemaphore getSemaphore(String name) {
		//ISemaphore controlSignal = instance.getSemaphore(name);
		LocalSemaphore controlSignal = new LocalSemaphore(0);
		controlSignal.setStore(this.localSignals);
		this.localSignals.put(name, controlSignal);
		return controlSignal;
	}
	
	public IAtomicLong getAtomicCounter(String name) {
		//return instance.getAtomicLong(name);
		return new LocalAtomicLong(name);
	}
	
	public Lock getLock(String key) {
		//return instance.getLock(key);
		return new ReentrantLock(true);
	}
	
	public HazelcastInstance getInstance() {
		return null;
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

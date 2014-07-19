package com.ericsson.raso.sef.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.ISemaphore;

public class CloudAwareClusterService implements CloudAwareCluster {

	//private HazelcastInstance instance;
	//private CloudAwareClusterService instance = null;
	
	private static final String HZ_CONFIG = "HZ_CONFIG";
	
	private ConcurrentHashMap<String, Map> localStore = new ConcurrentHashMap<String, Map>(); 
	private ConcurrentHashMap<String, ISemaphore> localSignals = new ConcurrentHashMap<String, ISemaphore>(); 
	
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
	
	public  <K, V> Map<K, V> getMap(String name) {
		//return instance.getMap(name);
		
		Map<K, V> namedMap = this.localStore.get(name);
		if (namedMap == null) {
			namedMap = new HashMap<K,V>();
			this.localStore.put(name, namedMap);
		}
		return namedMap;
			
	}
	
	public ExecutorService getDistributedService(String name) {
		//return instance.getExecutorService(name);
		return new ThreadPoolExecutor(50, 100, 30000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(5));
	}
	
	public ISemaphore getSemaphore(String name) {
		//ISemaphore controlSignal = instance.getSemaphore(name);
		LocalSemaphore controlSignal = (LocalSemaphore) this.localSignals.get(name);
		if (controlSignal == null) {
			controlSignal = new LocalSemaphore(name);
			controlSignal.setStore(this.localSignals);
			this.localSignals.put(name, controlSignal);
		}
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

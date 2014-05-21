package com.ericsson.raso.sef.watergate;

import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.core.CloudAwareCluster;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.hazelcast.core.IMap;

public class Watergate implements IWatergate {

	private CloudAwareCluster cluster;
	private static final String SlaManagementStore = "sla.mgmt.store";
	private static final String SYSTEM = "SYSTEM";
	private Sla systemSla = null;
	
	public Watergate(CloudAwareCluster cluster) {
		this.cluster = cluster;
	}

	public void start() {
		try {
			getSystemSla();
		} catch (FrameworkException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading system SLA, watergate cannot iniatialize", e);
		}
	}
	
	public void stop() {
		
	}
	
	
	public boolean slaCheck(String userId, String operationName) {
		
		boolean isInvalidCounter = true;
		boolean hasLimit = false;
		Sla sla;
		
		try{
		sla = getUserSla(userId, operationName);
		} catch (Exception e) {
			//TODO: Raise alarm that system cannot load user SLA and hence will reject requests
			return false;
		}
		String slaCounterKey = userId + operationName;
		
		if (slaCounterKey != null) {
			Map<String, ConcurrencyCounter> map = cluster
					.getMap(SlaManagementStore);
			ConcurrencyCounter counter = map.get(slaCounterKey);
			
			// Check counter expired or never created
			if (counter != null) {
				if (counter.isStillActive()) {
					//Check if counter is still under allowed limit
					hasLimit = counter.hasLimit();
					isInvalidCounter = false;
				} else {
					System.out.println("Not active");
					counter.reset();
					counter = null;
				}
			}
			
			// If counter expired or never created, create one
			if(isInvalidCounter) {
//			counter = new ConcurrencyCounter(sla.getCapacity(),
//					sla.getSlaUnit().getMilliseconds(),
//					System.currentTimeMillis(),
//					cluster.getAtomicCounter(slaCounterKey));
			}
			System.out.println(counter.isStillActive());
			System.out.println(slaCounterKey);
			map.put(slaCounterKey, counter);
		}
		
		return hasLimit;
	}

	public boolean counterExists(String userId) {
		return false;
	}

	private Sla getSystemSla() throws FrameworkException {
		SecureSerializationHelper fileSerializer = new SecureSerializationHelper();
		Sla sla = (Sla) fileSerializer.fetchFromFile(getLicenseFileLocation());
		return sla;
	}
	
	private Sla getUserSla(String userId,String operationName) throws FrameworkException {
		
		if(userId.equalsIgnoreCase(SYSTEM)) {
			if(systemSla == null) {
			systemSla = getSystemSla();
			}
			return systemSla;
		}
		
		Sla sla = null;
		String slaMeta = userId + operationName;
		Map<String, String> map = new TreeMap<String, String>();
		map.put("providerId", userId);
		map.put("slaMeta", slaMeta);
		
		String slaPeriod = SefCoreServiceResolver.getUserManagementService().getProviderSla(map);
		if (slaPeriod != null) {
			String[] slaArray = slaPeriod.split("-");
			if (slaArray != null && slaArray.length == 2) {
				sla = new Sla();
				sla.setCapacity(Integer.valueOf(slaArray[0]));
				sla.setSlaUnit(TimeUnit.valueOf(slaArray[1]));
			}
		}
		return sla;
	}
	
	private String getLicenseFileLocation() {
		String location = System.getenv("LICENSE_KEY");
		if(location == null)
			throw new RuntimeException("Unable to find the license file location. Ensure 'LICENSE_KEY' env variable is set with license file name and location");
		return location;
	}
}

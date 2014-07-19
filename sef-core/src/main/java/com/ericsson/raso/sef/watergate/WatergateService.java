package com.ericsson.raso.sef.watergate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ericsson.raso.sef.core.SecureSerializationHelper;

public class WatergateService implements IWatergate, IWatergateAdmin {
	
	private static final String SOLUTION_SLA = "SYSTEM";
	
	private Map<String, TrafficSla> watergateStore = null;
	
	//-------------------------------=========================== Service functions....

	@Override
	public void start() {
		String licenseLocation = System.getenv("LICENSE_KEY");
		if (licenseLocation == null) {
			//TODO: Logger - License File is not configured.. All requests will be rejected
			//TODO: Logger - SNMP Persistent Alarm that Watergate is not loaded
			return;
		}
		
		this.loadLicenseFile(licenseLocation);
		if (this.watergateStore == null) {
			//TODO: Logger - License File is configured but cannot be loaded.. All requests will be rejected
			//TODO: Logger - SNMP Persistent Alarm that Watergate is not initialized
		}
		
		for (TrafficSla sla: this.watergateStore.values()) 
			sla.joinCluster();
	}

	@Override
	public void stop() {
		this.watergateStore = null;
	}

	@Override
	public void refresh() {
		String licenseLocation = System.getenv("LICENSE_KEY");
		if (licenseLocation == null) {
			//TODO: Logger - License File is not configured.. Existing thresholds will continue
			//TODO: Logger - SNMP Transient Alarm that Watergate is not refreshed
			return;
		}
		
		Map<String, TrafficSla> newSlaThresholds = this.readFromFile(licenseLocation);
		if (newSlaThresholds == null) {
			//TODO: Logger - License File is configured but cannot be loaded.. Existing thresholds will continue
			//TODO: Logger - SNMP Transient Alarm that Watergate is not refreshed
			return;
		}
		
		for (String newSlaId: newSlaThresholds.keySet()) {
			if (this.watergateStore.containsKey(newSlaId)) {
				TrafficSla controlledSla = this.watergateStore.get(newSlaId);
				controlledSla.updateThreshold(newSlaThresholds.get(newSlaId));
			}
		}
		
	}

	@Override
	public boolean slaCheck(String userId, String interfaceName, String operationName) {
		
		if (this.watergateStore == null)
			return false;
		
		String contextKey = this.getKey(userId, interfaceName, operationName);
		if (contextKey == null)
			return false;
		
		TrafficSla systemWide = this.watergateStore.get(SOLUTION_SLA);
		TrafficSla systemWideInterface = this.watergateStore.get(SOLUTION_SLA + ":" + interfaceName);
		TrafficSla contextSpecific = this.watergateStore.get(contextKey);
		
		if (systemWide == null) {
			//TODO: Logger - system wide license not found... Cannot allow any request
			//TODO: Logger - send out Persistent SNMP Alarm 
			return false;
		}
		
		if (systemWideInterface == null) {
			//TODO: Logger - system wide license not found... Cannot allow any request
			//TODO: Logger - send out Persistent SNMP Alarm 
			return false;
		}
		
	
		if (systemWide.isIngressAllowed() && systemWideInterface.isIngressAllowed()) {
			if (contextSpecific != null) {
				if (contextSpecific.isIngressAllowed()) 
					contextSpecific.updateIngress();
			} else {
				//TODO: Logger - user & context specific SLA not found... Cannot allow this request...
				return false;
			}
			systemWide.updateIngress();
			systemWideInterface.updateIngress();
			//TODO: Logger - Clear any previous Persistent SNMP Alarm on licensed capacity...
			return true;
		} else {
			//TODO: Logger - system ran out licensed capacity... Wait for abate...
			//TODO: Logger - Persistent SNMP Alarm for licensed capacity...
			return false;
		}
	}
	
	//-------------------------------=========================== Admin functions....
	
	@Override
	public boolean saveTrafficSla(String user, String interfaceName, String operationName, int threshold, int duration, TimeUnit timeUnit) {
		
		if (operationName != null && interfaceName == null) {
			//TODO: Logger - operationName cannot be defined without interfaceName...
			return false;
		}
		
		if (user != null && interfaceName != null && operationName != null) {
			this.watergateStore.put(user + "-" + interfaceName + "-" + operationName, new TrafficSla(user, interfaceName, operationName, threshold, duration, timeUnit));
			return true;
		} else if (user != null && interfaceName != null) {
			this.watergateStore.put(user + "-" + interfaceName, new TrafficSla(user, interfaceName, operationName, threshold, duration, timeUnit));
			return true;
		} else if (user != null) {
			this.watergateStore.put(user, new TrafficSla(user, interfaceName, operationName, threshold, duration, timeUnit));
			return true;
		} else
			return false;
	}

	@Override
	public boolean removeTrafficSla(String user, String interfaceName, String operationName) {
		if (user == null || (operationName != null && interfaceName == null)) {
			//TODO:  Logger - wrong values to create key...
			return false;
		}
		
		if (user != null && interfaceName != null && operationName != null) {
			this.watergateStore.remove(user + "-" + interfaceName + "-" + operationName);
			return true;
		} else if (user != null && interfaceName != null) {
			this.watergateStore.remove(user + "-" + interfaceName);
			return true;
		} else if (user != null) {
			this.watergateStore.remove(user);
			return true;
		} else
			return false;
		
	}

	@Override
	public TrafficSla getTrafficSla(String user, String interfaceName, String operationName) {
		
		if (user != null && interfaceName != null && operationName != null)
			return this.watergateStore.get(user + "-" + interfaceName + "-" + operationName);
		else if (user != null && interfaceName != null)
			return this.watergateStore.get(user + "-" + interfaceName);
		else if (user != null)
			return this.watergateStore.get(user);
		else
			return null;
	}

	@Override
	public boolean createLicenseFile(String licenseUri) {
		if (this.watergateStore == null)
			return false;
		
		try {
			SecureSerializationHelper serializationHelper = new SecureSerializationHelper();
			serializationHelper.persistToFile(licenseUri, (Serializable) this.watergateStore);
		} catch (Exception e) {
			//TODO: - Logger - unable to write license file
			return false;
		}
		
		return true;
	}

	@Override
	public boolean loadLicenseFile(String licenseUri) {
		Map<String, TrafficSla> slaThresholds = this.readFromFile(licenseUri);
		if (slaThresholds == null) {
			this.watergateStore = new ConcurrentHashMap<String, TrafficSla>();
			return false;
		}
		
		this.watergateStore = slaThresholds;
		return true;
	}

	@Override
	public Collection<TrafficSla> getAllTrafficSla() {
		return this.watergateStore.values();
	}
	
	
	//-------------------------------=========================== private utlitarians....
	private Map<String, TrafficSla> readFromFile(String uri) {
		try { 
			SecureSerializationHelper serializationHelper = new SecureSerializationHelper();
			return (Map<String, TrafficSla>) serializationHelper.fetchFromFile(uri);
		} catch (Exception e) {
			//TODO: Logger - unable to read from the specified file....
			return null;
		}
	}
	
	private String getKey(String user, String interfaceName, String operationName) {
		if (user != null && interfaceName != null && operationName != null) {
			return (user + ":" + interfaceName + ":" + operationName);
		} else if (user != null && interfaceName != null) {
			return (user + ":" + interfaceName);
		} else if (user != null) {
			return (user);
		} else
			return null;

	}

}

package com.ericsson.raso.sef.watergate;

import java.util.Collection;

public interface IWatergateAdmin {
	
	public abstract boolean saveTrafficSla(String user, String interfaceName, String operationName, int threshold, int duration, TimeUnit timeUnit);
	
	public abstract boolean removeTrafficSla(String user, String interfaceName, String operationName);
	
	public abstract TrafficSla getTrafficSla(String user, String interfaceName, String operationName);
	
	public abstract boolean createLicenseFile(String licenseUri);
	
	public abstract boolean loadLicenseFile(String licenseUri);
	
	public abstract Collection<TrafficSla> getAllTrafficSla();

}

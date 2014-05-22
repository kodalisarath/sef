package com.ericsson.raso.sef.watergate;

public interface IWatergate {

	public void start();
	public void stop();
	public void refresh();
	
	/**
	 * Perform an SLA check if system is under allowed concurrency & user is under configured SLA
	 * 
	 * @param userid SLA check is performed against the user
	 * @param interfaceName specifies where the operation is being invoked.
	 * @param operationName specifies the operation name for which SLA check to performed. NULL allowed
	 * @return true if system can allow this request to be processed
	 */
	public boolean slaCheck(String userId, String interfaceName, String operationName);
	
}

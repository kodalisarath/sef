package com.ericsson.sef.diameter;

import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.raso.sef.core.lb.LoadBalancerPool;


public interface ScapChargingApi {
	
	public static String Service_Context_Id = "SCAP_V.2.0@ericsson.com";
	
	void start();
	
	void stop();

	Ccr createScapCcr(String sessionId, String hostId);
	
	Ccr createDummyCcr();
	
	//Diameter getDiameterConfig();
	
	LoadBalancerPool getLoadBalancerPool();
	

}

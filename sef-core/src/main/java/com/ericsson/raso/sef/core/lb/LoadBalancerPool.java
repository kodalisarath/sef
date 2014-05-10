package com.ericsson.raso.sef.core.lb;

public interface LoadBalancerPool {
	
	Member getMemberById(String hostId);
	
	Member getMemberBySite(String site);
	
}

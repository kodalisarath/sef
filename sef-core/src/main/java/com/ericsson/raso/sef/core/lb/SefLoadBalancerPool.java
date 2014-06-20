package com.ericsson.raso.sef.core.lb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SefLoadBalancerPool implements LoadBalancerPool {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, Member> hostRouteMap = new HashMap<String, Member>();
	private Map<String, List<Member>> siteMap = new HashMap<String, List<Member>>();
	
	private Map<String, LoadBalancer> loadBalancerMap = new HashMap<String, LoadBalancer>();
	
	public SefLoadBalancerPool(List<Member> routes) {
		init(routes);
	}
	
	private void init(List<Member> routes) {
		for (Member Member : routes) {
			hostRouteMap.put(Member.getHostId(), Member);
			
			List<Member> list = siteMap.get(Member.getSiteId());
			if(list == null) {
				list = new ArrayList<Member>();
				siteMap.put(Member.getSiteId(), list);
			}
			list.add(Member);
		}
	}
	
	protected void addToPool(String uri) {
		Member route = getMemberByUri(uri);
		if(route != null) {
			LoadBalancer loadBalancer = loadBalancerMap.get(route.getSiteId());
			if(loadBalancer == null) {
				loadBalancer = new RoundRobinLoadBalancer(route.getSiteId());
				loadBalancerMap.put(route.getSiteId(), loadBalancer);
			}
			
			if(!loadBalancer.getMembers().contains(route)) {
				loadBalancer.getMembers().add(route);
			}
		}
	}

	protected void removeFromPool(String uri) {
		Member route = getMemberByUri(uri);
		if(route != null) {
			LoadBalancer loadBalancer = loadBalancerMap.get(route.getSiteId());
			if(loadBalancer == null) {
				loadBalancer = new RoundRobinLoadBalancer(route.getSiteId());
				loadBalancerMap.put(route.getSiteId(), loadBalancer);
			}
			loadBalancer.getMembers().remove(route);
		}
	}
	
	
	private Member getMemberByUri(String uri) {
		for (Member route : hostRouteMap.values()) {
			if(uri.contains(route.getHostId())) {
				return route;
			}
		}
		return null;
	}



	@Override
	public Member getMemberById(String hostId) {
		// TODO Auto-generated method stub
		return hostRouteMap.get(hostId);
	}

	@Override
	public Member getMemberBySite(String site) {
		LoadBalancer balancer = loadBalancerMap.get(site);
		return balancer.chooseRoute();
	}
}

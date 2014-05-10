package com.ericsson.raso.sef.core.lb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefaultLoadBalancerPool implements LoadBalancerPool {
	
	private Map<String, Member> hostMap = new TreeMap<String, Member>();
	private Map<String, List<Member>> siteMap = new TreeMap<String, List<Member>>();
	
	private Map<String, LoadBalancer> loadBalancerMap = new TreeMap<String, LoadBalancer>();
	
	public DefaultLoadBalancerPool(List<Member> members) {
		init(members);
	}
	
	private void init(List<Member> members) {
		for (Member member : members) {
			hostMap.put(member.getHostId(), member);
			
			List<Member> list = siteMap.get(member.getSiteId());
			if(list == null) {
				list = new ArrayList<Member>();
				siteMap.put(member.getSiteId(), list);
			}
			list.add(member);
		}
	}
	
	protected void addToPool(String uri) {
		Member member = getMemberbyUri(uri);
		if(member != null) {
			LoadBalancer loadBalancer = loadBalancerMap.get(member.getSiteId());
			if(loadBalancer == null) {
				loadBalancer = new RoundRobinLoadBalancer(member.getSiteId());
				loadBalancerMap.put(member.getSiteId(), loadBalancer);
			}
			
			if(!loadBalancer.getMembers().contains(member)) {
				loadBalancer.getMembers().add(member);
			}
		}
	}

	protected void removeFromPool(String uri) {
		Member member = getMemberbyUri(uri);
		if(member != null) {
			LoadBalancer loadBalancer = loadBalancerMap.get(member.getSiteId());
			if(loadBalancer == null) {
				loadBalancer = new RoundRobinLoadBalancer(member.getSiteId());
				loadBalancerMap.put(member.getSiteId(), loadBalancer);
			}
			loadBalancer.getMembers().remove(member);
		}
	}
	
	@Override
	public Member getMemberById(String hostId) {
		return hostMap.get(hostId);
	}
	
	private Member getMemberbyUri(String uri) {
		for (Member route : hostMap.values()) {
			if(uri.contains(route.getHostId())) {
				return route;
			}
		}
		return null;
	}

	@Override
	public Member getMemberBySite(String site) {
		LoadBalancer balancer = loadBalancerMap.get(site);
		return balancer.getRoute();
	}
}

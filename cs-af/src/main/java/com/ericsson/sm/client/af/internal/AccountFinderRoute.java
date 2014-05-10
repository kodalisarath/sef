package com.ericsson.sm.client.af.internal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.config.Section;
import com.ericsson.raso.sef.core.lb.LoadBalancerPool;
import com.ericsson.raso.sef.core.lb.Member;
import com.ericsson.sm.client.af.DnsServiceResolver;

public class AccountFinderRoute  {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private LoadBalancerPool loadBalancerPool;
	
	public AccountFinderRoute() {
	
	}
	
	@SuppressWarnings("unused")
	private void init() {
		List<Member> members = createMembers();
		loadBalancerPool = new DnsLoadBalancerPool(members);
	}
	
	public DnsAddress getDns(String msisdn, String site) throws SmException {
		DnsAddress dns = new DnsAddress();
		dns.isUseTcp();
		
		if(site != null) {
			Member member = loadBalancerPool.getMemberBySite(site);
			if(member != null) {
				dns.setIp(member.getAddress());
			}
		}
		
		return dns;
	}
	
	
	private List<Member> createMembers() {
		
		List<Member> members = new ArrayList<>();
		//assuming max 10 end points can be configured 
		for (int i = 1; i < 11; i++) {
			String sectionName = "dns" + i;
			IConfig config = DnsServiceResolver.getConfig();
			Section dns = config.getSection(sectionName);
			if (dns == null)
				continue;
			
			String realm = config.getValue(sectionName, "realm");
			String address = config.getValue(sectionName, "address");
			String hostId = config.getValue(sectionName, "hostId");
			String site = config.getValue(sectionName, "site");
			String defaultsite = config.getValue(sectionName, "defaultSite");
			boolean isDefault = false;
			if(defaultsite != null && defaultsite.equalsIgnoreCase("true")) isDefault = true;
			
			Member member = new Member();
			member.setRealm(realm);
			member.setAddress(address);
			member.setHostId(hostId);
			member.setSiteId(site);
			member.setDefaultSite(isDefault);
			members.add(member);
			
		}
		return members;
	}
}

package com.ericsson.sm.client.af.internal;

import java.util.List;

import com.ericsson.raso.sef.core.lb.Member;
import com.ericsson.raso.sef.core.lb.DefaultLoadBalancerPool;

public class DnsLoadBalancerPool extends DefaultLoadBalancerPool {

	public DnsLoadBalancerPool(List<Member> members) {
		super(members);
		for (Member member : members) {
			addToPool(member.getHostId());
		}
	}
}

package com.ericsson.raso.sef.core.lb;

import java.util.List;

public interface LoadBalancer {
	
	void addMember(Member member);
	
	void removeMember(Member member);
	
	List<Member> getMembers();
	
	Member getRoute();

	Member chooseRoute();
}

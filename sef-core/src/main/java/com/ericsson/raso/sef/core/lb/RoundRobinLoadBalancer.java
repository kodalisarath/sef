package com.ericsson.raso.sef.core.lb;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoundRobinLoadBalancer implements LoadBalancer {

	private String name;
	private AtomicInteger counter = new AtomicInteger(0);
	private final List<Member> pool = new CopyOnWriteArrayList<Member>();
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public RoundRobinLoadBalancer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void addMember(Member member) {
		pool.add(member);
	}

	@Override
	public void removeMember(Member member) {
		pool.remove(member);
	}

	@Override
	public List<Member> getMembers() {
		return pool;
	}

	@Override
	public Member getRoute() {
		int size = pool.size();
		logger.debug("Pool Size: {}", size);
		if (size <= 0) {
			throw new IllegalStateException("[RoundRobinLoadBalancer] Pool is empty!.");
		}

		int target = counter.getAndIncrement();
		while (target >= size) {
			counter.set(0); // reset counter
			target = counter.getAndIncrement();
		}

		return pool.get(target);
	}

	@Override
	public Member chooseRoute() {
		int size = pool.size();
		logger.debug("Pool Size: {}", size);
		if (size <= 0) {
			throw new IllegalStateException("[RoundRobinLoadBalancer] Pool is empty!.");
		}

		int target = counter.getAndIncrement();
		while (target >= size) {
			counter.set(0); // reset counter
			target = counter.getAndIncrement();
		}

		return pool.get(target);
	}

}

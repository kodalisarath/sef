package com.ericsson.raso.sef.watergate;

import java.io.Serializable;

import com.hazelcast.core.IAtomicLong;

import com.ericsson.raso.sef.core.CloudAwareCluster;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class TrafficSla implements Serializable {
	private static final long	serialVersionUID	= -2730750560646425894L;


	private static final String		SLA_START			= "slaStart";
	private static final String		SLA_COUNTER			= "slaCounter";
	private static final String		SLA_VALIDITY		= "slaValidity";

	private String					user				= null;
	private String					interfaceName		= null;
	private String					operationName		= null;
	private int						threshold			= 0;
	private int						duration			= 0;
	private TimeUnit				timeUnit			= null;

	private transient IAtomicLong slaStart = null;
	private transient IAtomicLong slaValidity = null;
	private transient IAtomicLong slaCounter = null;
	
	public TrafficSla(String user, String interfaceName, String operationName, int threshold, int duration, TimeUnit timeUnit) {
		this.user = user;
		this.interfaceName = interfaceName;
		this.operationName = operationName;
		this.threshold = threshold;
		this.duration = duration;
		this.timeUnit = timeUnit;
	}
	
	public boolean isIngressAllowed() {
		long curTime = System.currentTimeMillis(); 
		if (curTime > this.slaValidity.get())
			this.spinSla(curTime);
		
		return ((this.threshold - this.slaCounter.get()) < 1);
	}

	public void updateIngress() {
		this.slaCounter.incrementAndGet();
	}
	
	
	public void spinSla(long curTime) {
		CloudAwareCluster cluster = SefCoreServiceResolver.getCloudAwareCluster();
		
		if (this.slaStart == null) 
			this.slaStart = cluster.getAtomicCounter(this.getKey(SLA_START));
		this.slaStart.set(curTime);
		
		if (this.slaCounter == null) 
			this.slaCounter = cluster.getAtomicCounter(this.getKey(SLA_COUNTER));
		this.slaCounter.set(0);
		
		if (this.slaValidity == null)
			this.slaValidity = cluster.getAtomicCounter(this.getKey(SLA_VALIDITY));
		this.slaValidity.set(curTime + (this.duration * this.timeUnit.longValue())); 
	}

	
	public void updateThreshold(TrafficSla other) {
		this.threshold = other.threshold;
		this.duration = other.duration;
		this.timeUnit = other.timeUnit;
		
		if (this.slaStart != null) {
			this.slaValidity.set(this.slaStart.get() + (this.duration * this.timeUnit.longValue()));
		}
	}
	
	public void joinCluster() {
		CloudAwareCluster cluster = SefCoreServiceResolver.getCloudAwareCluster();

		this.slaStart = cluster.getAtomicCounter(this.getKey(SLA_START));
		this.slaCounter = cluster.getAtomicCounter(this.getKey(SLA_COUNTER));
		this.slaValidity = cluster.getAtomicCounter(this.getKey(SLA_VALIDITY));
	}

	
	
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public IAtomicLong getSlaValidity() {
		return slaValidity;
	}

	public void setSlaValidity(IAtomicLong slaValidity) {
		this.slaValidity = slaValidity;
	}

	public IAtomicLong getSlaCounter() {
		return slaCounter;
	}

	public void setSlaCounter(IAtomicLong slaCounter) {
		this.slaCounter = slaCounter;
	}

	private String getKey(String slaParam) {
		if (user != null && interfaceName != null && operationName != null) {
			return (slaParam + ":" + user + ":" + interfaceName + ":" + operationName);
		} else if (user != null && interfaceName != null) {
			return (slaParam + ":" + user + ":" + interfaceName);
		} else if (user != null) {
			return (slaParam + ":" + user);
		} else
			return null;

	}

}

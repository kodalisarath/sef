package com.ericsson.sef.scheduler;

import org.quartz.Scheduler;
import org.springframework.context.SmartLifecycle;

public interface SefScheduler extends SmartLifecycle {
	
	Scheduler getDelegate();

}

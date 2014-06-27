package com.ericsson.sef.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.config.Property;
import com.ericsson.raso.sef.core.config.Section;
import com.ericsson.raso.sef.smart.subscription.response.HelperConstant;
public class DefaultSefScheduler implements SefScheduler {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Scheduler scheduler;
	
	private boolean started;
	
	@Override
	public Scheduler getDelegate() {
		return scheduler;
	}

	@Override
	public void start() {
		Properties properties = new Properties();
		//StackParams params = config.getStackParams();
		
		List<String> iplist= com.ericsson.sef.scheduler.config.SefCoreUtil.getServerIPList();
		 
		boolean isServer = false;
		IConfig config = SefCoreServiceResolver.getConfigService();
		Section section = config.getSection(HelperConstant.QUARTZ_INTERFACE);
		int i = 1;
		for (Property property : config.getProperties(section)) {
			if (property.getKey().equalsIgnoreCase("allowedInterfaces_"+i)) {
				
				List<String> values = new ArrayList<String>();
				values.add(property.getValue());
				if (com.ericsson.sef.scheduler.config.SefCoreUtil.intersection(iplist, values).size() > 0) {
					isServer = true;
				}
				i++;
			}
		}

		//section = config.getSection(SmartConstants.QUARTZ_SERVER);
		//i = 1;

		if (isServer) {
			for (Property propertyServer : config.getProperties(config.getSection(HelperConstant.QUARTZ_SERVER))) {
				properties.put(propertyServer.getKey(), propertyServer.getValue());
			}
		} else {
			for (Property propertyServer : config.getProperties(config.getSection(HelperConstant.QUARTZ_CLIENT))) {
				properties.put(propertyServer.getKey(), propertyServer.getValue());
			}
		}
	
		try {
			SchedulerFactory factory = new StdSchedulerFactory(properties);
			scheduler = factory.getScheduler();
			if(isServer) {
				scheduler.start();
				started = true;
			}
			log.debug("Schedular Config loaded");
		} catch (SchedulerException e) {
			log.error("Error Starting Scheduler. ", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop() {
		if(started) {
			try {
				scheduler.shutdown();
				started = false;
			} catch (SchedulerException e) {
				log.error("Error Stopping Scheduler. ", e);
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public boolean isRunning() {
		return started;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		this.stop();
	}

	@Override
	public int getPhase() {
		return 0;
	}
}

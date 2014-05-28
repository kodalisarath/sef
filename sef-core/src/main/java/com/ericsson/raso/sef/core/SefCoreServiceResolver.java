package com.ericsson.raso.sef.core;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.auth.PrivilegeManager;
import com.ericsson.raso.sef.auth.service.IPrivilegeManager;
import com.ericsson.raso.sef.auth.service.IUserStore;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.raso.sef.core.db.service.UserManagementService;
import com.ericsson.raso.sef.watergate.IWatergate;

public class SefCoreServiceResolver implements ApplicationContextAware {

	public static ApplicationContext context; 
	private static int maxThreadsForCurrentOs = 0;
	private static ExecutorService localExecutor = null;
	
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SefCoreServiceResolver.context = context;
	}

	public static UserManagementService getUserManagementService() {
		return SefCoreServiceResolver.context.getBean(UserManagementService.class);
	}
	
	public static IWatergate getWatergateService() {
		return SefCoreServiceResolver.context.getBean(IWatergate.class);
	}
	
	public static IConfig getConfigService() {
		return SefCoreServiceResolver.context.getBean(IConfig.class);
	}
	
	public static IPrivilegeManager getPrivilegeManagementService() {
		//return SefCoreServiceResolver.context.getBean(IPrivilegeManager.class);
		return new PrivilegeManager("Z:\\Common Share\\Projects\\raso-cac\\rasocac\\privilegeStore.zccm");
	}
	
	public static IUserStore getUserStore() {
		return SefCoreServiceResolver.context.getBean(IUserStore.class);
	}

	public static ExecutorService getExecutorService(String name) {
		if (maxThreadsForCurrentOs == 0)
			maxThreadsForCurrentOs = getMaxThreadLimit();
		
		if (ManagementFactory.getThreadMXBean().getThreadCount() <= maxThreadsForCurrentOs) {
			if (localExecutor == null) {
				localExecutor = new ThreadPoolExecutor(25, 25, 2400, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(5));
			}
			return localExecutor;
		}
		
		return SefCoreServiceResolver.context.getBean(CloudAwareCluster.class).getDistributedService(name);
	}
	
	private static int getMaxThreadLimit() {
		String currOsName = System.getProperty("os.name");
		
		if (currOsName == null || currOsName.isEmpty())
			return 400;
		
		if (currOsName.contains("inux"))
			return 600;
		
		if (currOsName.contains("nix"))
			return 800;
		
		if (currOsName.contains("olaris"))
			return 1100;
		
		if (currOsName.contains("indows"))
			return 1800;
		
		
		return 400; // absolute fallback
	}

	public static SubscriberService getSusbcriberStore() {
		return SefCoreServiceResolver.context.getBean(SubscriberService.class);
	}
	
	public static CloudAwareCluster getCloudAwareCluster() {
		return SefCoreServiceResolver.context.getBean(CloudAwareCluster.class);
	}
	
	//TODO: Add other services - Logger, Request Context
	
	
}

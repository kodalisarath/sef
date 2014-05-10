package com.ericsson.raso.sef.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.auth.PrivilegeManager;
import com.ericsson.raso.sef.auth.service.IPrivilegeManager;
import com.ericsson.raso.sef.auth.service.IUserStore;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.service.UserManagementService;
import com.ericsson.raso.sef.watergate.IWatergate;

public class SefCoreServiceResolver implements ApplicationContextAware {

	public static ApplicationContext context; 
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
	//TODO: Add other services - Logger, User Store, Request Context
	
	
}

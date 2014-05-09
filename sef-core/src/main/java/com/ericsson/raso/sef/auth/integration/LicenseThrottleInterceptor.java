package com.ericsson.raso.sef.auth.integration;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class LicenseThrottleInterceptor extends AbstractPhaseInterceptor<Message>{

	public LicenseThrottleInterceptor() {
		super(Phase.RECEIVE);
	}
	
	public LicenseThrottleInterceptor(String p) {
		super(p);
	}
	@Override
	public void handleMessage(Message message) throws Fault {
		
		if(!SefCoreServiceResolver.getWatergateService().slaCheck("SYSTEM", "")) {
			//TODO: Raise alarm about license throttling
			throw new SecurityException("License capacity exceeded!!");
		}
		
	}

}

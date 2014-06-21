package com.ericsson.raso.sef.cg.engine.web;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class ReloadArtifactServiceImpl implements ReloadArtifactService {
	
	private static Logger logger = LoggerFactory.getLogger(ReloadArtifactServiceImpl.class);
	//private static Logger snmpLogger = Logger.getLogger("API-SNMP");
	private Exchange exchange;
	
	
	public void reload() throws Exception {
		
		logger.debug("Entering into  ReloadArtifactServiceImpl rest web service");
		
		try{
		
			
			logger.debug("  ReloadArtifactServiceImpl rest web service Execution Done..... ");
		
		}catch (Exception e) {
			
			logger.error(" Cause: " + e);
			throw e;
			
		}
		
	}
	
}

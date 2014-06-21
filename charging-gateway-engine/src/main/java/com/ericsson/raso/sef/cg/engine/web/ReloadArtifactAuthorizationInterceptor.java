package com.ericsson.raso.sef.cg.engine.web;

import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Provider
public class ReloadArtifactAuthorizationInterceptor implements RequestHandler {
	private static Logger logger = LoggerFactory.getLogger(ReloadArtifactAuthorizationInterceptor.class);
	//private static Logger snmpLogger = Logger.getLogger("API-SNMP");
	
	
	private Properties properties = null;
	

	@Context	
	private HttpHeaders headers;

	@Override
	public Response handleRequest(Message message, ClassResourceInfo arg1) {
		
		//properties = Config.getInstance().getGlobalProperties();
		
		try {
			
		} catch (Exception e) {
			
			return createFaultResponse();
			
		}
		return null;
	}
	
	
	private Response createFaultResponse() {		
		logger.error("Authentication fail.");
		/*snmpLogger.log(Level.FATAL, new Event(EventCode.SYSTEM_PLUGIN_AUTHORIZE_INIT_FAIL.getEventCode(), "Authentication fail.", 
				EventCode.SYSTEM_PLUGIN_AUTHORIZE_INIT_FAIL.getMessage()));*/
		return Response.status(Response.Status.FORBIDDEN).build();	
	}
	
}

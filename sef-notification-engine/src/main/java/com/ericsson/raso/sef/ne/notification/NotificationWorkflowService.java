package com.ericsson.raso.sef.ne.notification;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.opensaml.ws.WSException;

import com.ericsson.raso.sef.core.Meta;

@WebService
public interface NotificationWorkflowService {
	
	@WebMethod(operationName = "processWorkflow")
	void processWorkflow(
			@WebParam(name = "msisdn")  String msisdn,
			@WebParam(name = "eventId") String eventId,
			@WebParam(name = "metas")  List<Meta> metas) throws WSException;

}

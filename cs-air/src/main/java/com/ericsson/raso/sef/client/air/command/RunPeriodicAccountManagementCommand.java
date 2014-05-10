package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.RunPeriodicAccountManagementRequest;
import com.ericsson.raso.sef.client.air.response.RunPeriodicAccountManagementResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class RunPeriodicAccountManagementCommand extends AbstractAirCommand<RunPeriodicAccountManagementResponse>{
	
	private RunPeriodicAccountManagementRequest request;
	
	public RunPeriodicAccountManagementCommand(RunPeriodicAccountManagementRequest request) {
		this.request = request;
	}
	
	@Override
	public RunPeriodicAccountManagementResponse execute() throws SmException {
		RunPeriodicAccountManagementResponse response = new RunPeriodicAccountManagementResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()), e.linkedException);
		}
		return null;
	}

}

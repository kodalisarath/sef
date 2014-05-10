package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.AddPeriodicAccountManagementDataReq;
import com.ericsson.raso.sef.client.air.response.AddPeriodicAccountManagementDataRes;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class AddPeriodicAccountManagementDataCmd extends AbstractAirCommand<AddPeriodicAccountManagementDataRes> {

	private AddPeriodicAccountManagementDataReq request;
	
	public AddPeriodicAccountManagementDataCmd(AddPeriodicAccountManagementDataReq request) {
		this.request = request;
	}

	@Override
	public AddPeriodicAccountManagementDataRes execute() throws SmException {
		AddPeriodicAccountManagementDataRes response = new AddPeriodicAccountManagementDataRes();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()), e.linkedException);
		}
		return null;
	}

}

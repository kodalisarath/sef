package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.UpdateBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.UpdateBalanceAndDateResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class UpdateBalanceAndDateCommand extends AbstractAirCommand<UpdateBalanceAndDateResponse> {
	
	private UpdateBalanceAndDateRequest request;

	public UpdateBalanceAndDateCommand(UpdateBalanceAndDateRequest request) {
		this.request = request;
	}

	@Override
	public UpdateBalanceAndDateResponse execute() throws SmException {
		UpdateBalanceAndDateResponse response = new UpdateBalanceAndDateResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(),
					new ResponseCode(e.code, e.getMessage()), e.linkedException);
		}
		return response;
	}

}

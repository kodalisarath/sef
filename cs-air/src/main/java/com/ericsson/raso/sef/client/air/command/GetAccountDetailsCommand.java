package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.GetAccountDetailsRequest;
import com.ericsson.raso.sef.client.air.response.GetAccountDetailsResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class GetAccountDetailsCommand extends AbstractAirCommand<GetAccountDetailsResponse> {

	private GetAccountDetailsRequest request;
	
	public GetAccountDetailsCommand(GetAccountDetailsRequest request) {
		this.request = request;
	}

	@Override
	public GetAccountDetailsResponse execute() throws SmException {
		GetAccountDetailsResponse response = new GetAccountDetailsResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()));
		}
		return response;
	}
}

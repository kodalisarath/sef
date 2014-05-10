package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.GetBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.GetBalanceAndDateResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class GetBalanceAndDateCommand extends AbstractAirCommand<GetBalanceAndDateResponse> {

	private GetBalanceAndDateRequest request;

	public GetBalanceAndDateCommand(GetBalanceAndDateRequest request) {
		this.request = request;
	}

	@Override
	public GetBalanceAndDateResponse execute() throws SmException {
		GetBalanceAndDateResponse response = new GetBalanceAndDateResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()));
		}
		return response;
	}
}

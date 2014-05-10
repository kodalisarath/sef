package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.GetOffersRequest;
import com.ericsson.raso.sef.client.air.response.GetOffersResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class GetOffersCommand extends AbstractAirCommand<GetOffersResponse> {

	private GetOffersRequest request;

	public GetOffersCommand(GetOffersRequest request) {
		this.request = request;
	}

	@Override
	public GetOffersResponse execute() throws SmException {
		GetOffersResponse response = new GetOffersResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()));
		}
		return response;
	}
}

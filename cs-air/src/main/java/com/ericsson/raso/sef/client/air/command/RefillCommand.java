package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.RefillRequest;
import com.ericsson.raso.sef.client.air.response.RefillResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class RefillCommand extends AbstractAirCommand<RefillResponse> {

	private RefillRequest request;

	public RefillCommand(RefillRequest refillRequest) {
		this.request = refillRequest;
	}

	@Override
	public RefillResponse execute() throws SmException {
		RefillResponse response = new RefillResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(),
					new ResponseCode(e.code, e.getMessage()), e.linkedException);
		}

		return response;
	}

}

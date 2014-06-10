package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.DeleteSubscriberRequest;
import com.ericsson.raso.sef.client.air.response.DeleteSubscriberResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class DeleteSubscriberCommand extends AbstractAirCommand<DeleteSubscriberResponse> {

	private DeleteSubscriberRequest request;
	
	public DeleteSubscriberCommand(DeleteSubscriberRequest request) {
		this.request = request;
	}

	@Override
	public DeleteSubscriberResponse execute() throws SmException {
		DeleteSubscriberResponse response = new DeleteSubscriberResponse(endpointId);
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()));
		}
		return response;
	}
}

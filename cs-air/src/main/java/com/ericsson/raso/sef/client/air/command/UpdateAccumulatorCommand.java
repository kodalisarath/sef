package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.UpdateAccumulatorRequest;
import com.ericsson.raso.sef.client.air.response.UpdateAccumulatorResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class UpdateAccumulatorCommand extends AbstractAirCommand<UpdateAccumulatorResponse> {

	private UpdateAccumulatorRequest request;
	
	public UpdateAccumulatorCommand(UpdateAccumulatorRequest request) {
		this.request = request;
	}

	@Override
	public UpdateAccumulatorResponse execute() throws SmException {
		UpdateAccumulatorResponse response = new UpdateAccumulatorResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()), e.linkedException);
		}
		return response;
	}
}
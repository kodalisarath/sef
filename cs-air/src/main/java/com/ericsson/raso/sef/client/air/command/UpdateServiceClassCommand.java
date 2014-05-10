package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.UpdateServiceClassRequest;
import com.ericsson.raso.sef.client.air.response.UpdateServiceClassResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class UpdateServiceClassCommand extends AbstractAirCommand<UpdateServiceClassResponse> {

	private UpdateServiceClassRequest request;
	
	public UpdateServiceClassCommand(UpdateServiceClassRequest request) {
		this.request = request;
	}

	@Override
	public UpdateServiceClassResponse execute() throws SmException {
		UpdateServiceClassResponse response = new UpdateServiceClassResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()), e.linkedException);
		}
		return response;
	}

}

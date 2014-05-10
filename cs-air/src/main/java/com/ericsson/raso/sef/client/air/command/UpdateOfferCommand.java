package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.UpdateOfferRequest;
import com.ericsson.raso.sef.client.air.response.UpdateOfferResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class UpdateOfferCommand extends AbstractAirCommand<UpdateOfferResponse> {

	private UpdateOfferRequest request;
	
	public UpdateOfferCommand(UpdateOfferRequest request) {
		this.request = request;
	}

	@Override
	public UpdateOfferResponse execute() throws SmException {
		UpdateOfferResponse response = new UpdateOfferResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()), e.linkedException);
		}
		return response;
	}
}

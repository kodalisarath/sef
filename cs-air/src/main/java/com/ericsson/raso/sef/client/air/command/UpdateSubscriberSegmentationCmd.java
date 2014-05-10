package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.UpdateSubscriberSegmentRequest;
import com.ericsson.raso.sef.client.air.response.SegmentationResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class UpdateSubscriberSegmentationCmd extends AbstractAirCommand<SegmentationResponse> {
	
	private UpdateSubscriberSegmentRequest request;
	
	public UpdateSubscriberSegmentationCmd(UpdateSubscriberSegmentRequest request) {
		this.request = request;
	}

	@Override
	public SegmentationResponse execute() throws SmException {
		SegmentationResponse response = new SegmentationResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()), e.linkedException);
		}
		return response;
	}
}

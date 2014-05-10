package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.GetUsageThresholdsAndCountersRequest;
import com.ericsson.raso.sef.client.air.response.GetUsageThresholdsAndCountersResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class GetUsageThresholdsAndCountersCmd extends AbstractAirCommand<GetUsageThresholdsAndCountersResponse>{
	
	private GetUsageThresholdsAndCountersRequest request;
	
	public GetUsageThresholdsAndCountersCmd(GetUsageThresholdsAndCountersRequest request)
	{
		this.request = request;	
	}

	@Override
	public GetUsageThresholdsAndCountersResponse execute() throws SmException {
		GetUsageThresholdsAndCountersResponse response = new GetUsageThresholdsAndCountersResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()));
		}
		return response;
	}

}

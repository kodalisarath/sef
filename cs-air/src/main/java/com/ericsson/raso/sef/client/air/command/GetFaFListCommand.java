package com.ericsson.raso.sef.client.air.command;

import java.util.List;

import com.ericsson.raso.sef.client.air.internal.CsAirContext;
import com.ericsson.raso.sef.client.air.request.GetFaFListRequest;
import com.ericsson.raso.sef.client.air.response.FafInformationList;
import com.ericsson.raso.sef.client.air.response.GetFaFListResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public class GetFaFListCommand extends AbstractAirCommand<GetFaFListResponse> {
	
	private GetFaFListRequest request;

	public GetFaFListCommand(GetFaFListRequest request) {
		System.out.println("setting getfaflist");
		this.request = request;
	}

	@Override
	public GetFaFListResponse execute() throws SmException {
		GetFaFListResponse response = new GetFaFListResponse();
		try {
			CsAirContext.getAirClient().execute(request, response);
			System.out.println("FafChangeUnbarDate is:"  +response.getFafChangeUnbarDate());
			
			List<FafInformationList> res=response.getFafInformationList();
			System.out.println("response6 is:" +response.getResponseCode());
			for(FafInformationList flist: res)
			{	
							System.out.println("FafNumber:"+flist.getFafNumber());
							System.out.println("FafIndicator:"+flist.getFafIndicator());
							System.out.println("Owner:"+flist.getOwner());
							
			}
			Integer[] test=response.getNegotiatedCapabilities();
			for(Integer inttest:test)
			{			System.out.println("NegotiatedCapabilities is:" +inttest.intValue());
			}
			
			Integer[] test1=response.getAvailableServerCapabilities();
			for(Integer inttest:test1)
			{			System.out.println("AvailableServerCapabilities is:" +inttest.intValue());
			}
									
			
			
		} catch (XmlRpcException e) {
			throw new SmException(CsAirContext.getSection(), new ResponseCode(e.code, e.getMessage()));
		}
		return response;
	}
}



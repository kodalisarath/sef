package com.ericsson.sef.bes.api.subscription;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface SubscriptionManagement {

	@WebMethod(operationName = "purchaseProduct")
	void purchaseProduct(@WebParam(name = "msisdn") String msisdn,
			@WebParam(name = "offerId") String offerId,
			@WebParam(name = "meta") List<Meta> metas) throws WSException;

	@WebMethod(operationName = "discoverUserOffers")
	void discoverUserOffers(
			@WebParam(name = "msisdn") String msisdn,
			@WebParam(name = "prt") List<String> prt,
			@WebParam(name = "meta") List<Meta> metas) throws WSException;

	@WebMethod(operationName = "queryScheduledRequests")
	void queryPendingRequests(
			@WebParam(name = "msisdn") String msisdn,
			@WebParam(name = "startDate") Date startDate,
			@WebParam(name = "endDate") Date endDate
			) throws WSException;

	@WebMethod(operationName = "cancelPendingRequest")
	void cancelPendingRequest(@WebParam(name = "msisdn") String msisdn,
			@WebParam(name = "requestId") String offerId)

	throws WSException;

	@WebMethod(operationName = "queryUserSubscriptions")
	void queryUserSubscriptions(
			@WebParam(name = "msisdn") String msisdn,
			@WebParam(name = "prt") List<String> prt)

	throws WSException;

}

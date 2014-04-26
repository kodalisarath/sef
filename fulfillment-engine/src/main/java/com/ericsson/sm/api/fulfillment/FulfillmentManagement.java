package com.ericsson.sm.api.fulfillment;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface FulfillmentManagement {

	@WebMethod(operationName = "create")
	ActivationStatus create(
			String userId, 
			String msisdn, 
			String serviceId) throws WSException;
	
	@WebMethod(operationName = "update")
	ActivationStatus update(
			String userId, 
			String msisdn, 
			String serviceId) throws WSException;
	
	@WebMethod(operationName = "delete")
	ActivationStatus delete(
			String userId, 
			String msisdn, 
			String serviceId) throws WSException;
	
	@WebMethod(operationName = "get")
	ActivationStatus get(
			String userId, 
			String msisdn, 
			String serviceId) throws WSException;

}

package com.ericsson.sef.bes.api.fulfillment;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Product;

@WebService
public interface FulfillmentRequest {

	@WebMethod(operationName = "prepare")
	String prepare(String requestId, String msisdn, Product product);

	@WebMethod(operationName = "fulfill")
	String fulfill(String requestId, String msisdn, Product product);

	@WebMethod(operationName = "reverse")
	String reverse(String requestId, String msisdn, Product product);

	@WebMethod(operationName = "query")
	String query(String requestId, String msisdn, Product product);

	@WebMethod(operationName = "cancel")
	String cancel(String requestId, String msisdn, Product product);

}

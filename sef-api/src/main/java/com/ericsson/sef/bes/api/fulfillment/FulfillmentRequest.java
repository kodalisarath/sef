package com.ericsson.sef.bes.api.fulfillment;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;

@WebService
public interface FulfillmentRequest {

	@WebMethod(operationName = "prepare")
	String prepare(String requestId, String msisdn, Product product, List<Meta> metas);

	@WebMethod(operationName = "fulfill")
	String fulfill(String requestId, String msisdn, Product product, List<Meta> metas);

	@WebMethod(operationName = "reverse")
	String reverse(String requestId, String msisdn, Product product, List<Meta> metas);

	@WebMethod(operationName = "query")
	String query(String requestId, String msisdn, Product product, List<Meta> metas);

	@WebMethod(operationName = "cancel")
	String cancel(String requestId, String msisdn, Product product, List<Meta> metas);

}

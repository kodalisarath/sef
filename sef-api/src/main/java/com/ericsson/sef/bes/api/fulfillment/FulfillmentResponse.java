package com.ericsson.sef.bes.api.fulfillment;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Product;

@WebService
public interface FulfillmentResponse {

	@WebMethod(operationName = "prepare")
	void prepare(String requestId, List<Product> products);

	@WebMethod(operationName = "fulfill")
	void fulfill(String requestId, List<Product> products);

	@WebMethod(operationName = "reverse")
	void reverse(String requestId, List<Product> products);

	@WebMethod(operationName = "query")
	void query(String requestId, List<Product> products);

	@WebMethod(operationName = "cancel")
	void cancel(String requestId, List<Product> products);
}

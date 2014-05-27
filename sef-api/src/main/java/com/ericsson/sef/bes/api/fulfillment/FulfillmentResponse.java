package com.ericsson.sef.bes.api.fulfillment;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

@WebService
public interface FulfillmentResponse {

	@WebMethod(operationName = "prepare")
	void prepare(String correlationId, TransactionStatus exception, List<Product> products, List<Meta> metas);

	@WebMethod(operationName = "fulfill")
	void fulfill(String correlationId, TransactionStatus exception, List<Product> products, List<Meta> metas);

	@WebMethod(operationName = "reverse")
	void reverse(String correlationId, TransactionStatus exception, List<Product> products, List<Meta> metas);

	@WebMethod(operationName = "query")
	void query(String correlationId, TransactionStatus exception, List<Product> products, List<Meta> metas);

	@WebMethod(operationName = "cancel")
	void cancel(String correlationId, TransactionStatus exception, List<Product> products, List<Meta> metas);
}

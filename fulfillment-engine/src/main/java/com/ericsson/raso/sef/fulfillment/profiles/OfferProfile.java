package com.ericsson.raso.sef.fulfillment.profiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.UpdateOfferCommand;
import com.ericsson.raso.sef.client.air.request.UpdateOfferRequest;
import com.ericsson.raso.sef.client.air.response.UpdateOfferResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcInt;
import com.ericsson.sef.bes.api.entities.Product;

public class OfferProfile extends BlockingFulfillment<Product> {
	private static final long	serialVersionUID	= 3485101569265007348L;
	private static final Logger LOGGER = LoggerFactory.getLogger(OfferProfile.class);
	private static final String	OFFER_RESPONSE_CODE	= "OFFER_RESPONSE_CODE";
	private static final String	OFFER_RESULT	= "OFFER_RESULT";
	
	private Integer offerID;
	private Integer offerType;
	


	public OfferProfile(String name) {
		super(name);
	}

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		LOGGER.debug("Entering fufill of Offer Profile...");
		
		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));
		
		List<Product> products = new ArrayList<Product>();
		
		UpdateOfferRequest request = new UpdateOfferRequest();
		request.setOfferID(this.offerID);
		request.setOfferType(this.offerType);
		
		String requestParam = map.get("SUSBCRIBER_ID");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'SUBSCRIBER_ID' missing in request!!"));
		request.setSubscriberNumber(requestParam);
		request.setSubscriberNumberNAI(1);
		
		
		requestParam = map.get("OFFER_EXPIRY_DATE");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'OFFER_EXPIRY_DATE' missing in request!!"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			request.setExpiryDate(formatter.parse(requestParam));
		} catch (ParseException e1) {
			LOGGER.error("Bad Expiry Date: " + requestParam, e1);
			throw new FulfillmentException("ffe", new ResponseCode(1003, "runtime parameter 'OFFER_EXPIRY_DATE' bad in request!!"));
		}
		
		requestParam = map.get("OFFER_EXPIRY_DATE_RELATIVE");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'OFFER_EXPIRY_DATE_RELATIVE' missing in request!!"));
		try {
			request.setExpiryDateRelative(Integer.parseInt(requestParam));
		} catch (NumberFormatException e1) {
			LOGGER.error("Bad Expiry Date Relative: " + requestParam, e1);
			throw new FulfillmentException("ffe", new ResponseCode(1003, "runtime parameter 'OFFER_EXPIRY_DATE_RELATIVE' bad in request!!"));
		}
		
		requestParam = map.get("OFFER_EXPIRY_DATE_TIME");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'OFFER_EXPIRY_DATE_TIME' missing in request!!"));
		try {
			request.setExpiryDateTime(formatter.parse(requestParam));
		} catch (ParseException e1) {
			LOGGER.error("Bad Expiry Date Time: " + requestParam, e1);
			throw new FulfillmentException("ffe", new ResponseCode(1003, "runtime parameter 'OFFER_EXPIRY_DATE_TIME' bad in request!!"));
		}
		
		
		requestParam = map.get("OFFER_EXPIRY_TIME_RELATIVE");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'OFFER_EXPIRY_TIME_RELATIVE' missing in request!!"));
		request.setExpiryDateTimeRelative(new XmlRpcInt(requestParam));
	
		requestParam = map.get("OFFER_START_DATE_TIME");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'OFFER_START_DATE_TIME' missing in request!!"));
		try {
			request.setStartDateTime(formatter.parse(requestParam));
		} catch (ParseException e1) {
			LOGGER.error("Bad Start Date Time: " + requestParam, e1);
			throw new FulfillmentException("ffe", new ResponseCode(1003, "runtime parameter 'OFFER_START_DATE_TIME' bad in request!!"));
		}

		
		UpdateOfferCommand command = new UpdateOfferCommand(request);
		UpdateOfferResponse response = null;
		try {
			response = command.execute();
		} catch (SmException e1) {
			LOGGER.error("Execution Failure for update offer!!", e1);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		
		
		
		LOGGER.debug("Sending back response to fulfill now...");
		return createReponse(e, response);
	}

	private List<Product> createReponse(Product p, UpdateOfferResponse response) {
		List<Product> products = new ArrayList<Product>();
		
		Product product = new Product();
		product.setResourceName(p.getResourceName());
		product.setQuotaDefined(-1);
		product.setValidity(-1);
		
		Map<String, String> offerInfo = new HashMap<String, String>();
		offerInfo.put(OFFER_RESPONSE_CODE, "" + response.getResponseCode());
		offerInfo.put(OFFER_RESULT, "" + response.getResult());
		
		product.setMetas(offerInfo);
		products.add(product);
		return products;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}


	public Integer getOfferID() {
		return offerID;
	}

	public void setOfferID(Integer offerID) {
		this.offerID = offerID;
	}

	public Integer getOfferType() {
		return offerType;
	}

	public void setOfferType(Integer offerType) {
		this.offerType = offerType;
	}

		

	
}

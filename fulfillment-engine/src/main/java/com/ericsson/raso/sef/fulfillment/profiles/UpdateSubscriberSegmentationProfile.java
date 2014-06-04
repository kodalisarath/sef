package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.UpdateSubscriberSegmentationCmd;
import com.ericsson.raso.sef.client.air.request.ServiceOffering;
import com.ericsson.raso.sef.client.air.request.UpdateSubscriberSegmentRequest;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class UpdateSubscriberSegmentationProfile extends BlockingFulfillment<Product>{

	private static final long serialVersionUID = -4012237785041809835L;
	private static final Logger logger = LoggerFactory.getLogger(UpdateSubscriberSegmentationProfile.class);
	
	List<ServiceOffering> serviceOfferings = new ArrayList<ServiceOffering>();
	
	protected UpdateSubscriberSegmentationProfile(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map)
			throws FulfillmentException {
		logger.info("Entering fulfilment for UpdateSubscriberSegmentation");
		
		List<Product> products = new ArrayList<Product>();
		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));
		
		UpdateSubscriberSegmentRequest request = new UpdateSubscriberSegmentRequest();
		String msisdn = map.get("msisdn");
		request.setSubscriberNumber(msisdn);
		request.setServiceOfferings(serviceOfferings);
		try {
			new UpdateSubscriberSegmentationCmd(request).execute();
		} catch (SmException e1) {
			logger.error("Refill execution failed!!", e1);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		} 
		return products;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map)
			throws FulfillmentException {
		logger.info("Entering prepare for UpdateSubscriberSegmentation");
		List<Product> products = new ArrayList<Product>();
		return products;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map)
			throws FulfillmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map)
			throws FulfillmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}

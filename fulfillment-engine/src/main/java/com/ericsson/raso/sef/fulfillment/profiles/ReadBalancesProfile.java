package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.request.GetBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.GetBalanceAndDateResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.SubDedicatedInfo;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class ReadBalancesProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 5706705833688931767L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadBalancesProfile.class);

	
	public ReadBalancesProfile(String name) {
		super(name);
	}
	
	
	

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(1000, "Not Implemented!"));

	}


	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(1000, "Not Implemented!"));
	}


	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		
		LOGGER.debug("Query request for read balances...");
		
		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));
		
		
		GetBalanceAndDateRequest request = new GetBalanceAndDateRequest();
		String subscriberId = map.get("SUBSCRIBER_ID");
		if (subscriberId == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'SUBSCRIBER_ID' missing in request!!"));
		request.setSubscriberNumber(subscriberId);
		request.setSubscriberNumberNAI(1);
		
		GetBalanceAndDateResponse response = null;
		GetBalanceAndDateCommand command = new GetBalanceAndDateCommand(request);
		
		try {
			response = command.execute();
		} catch (SmException e1) {
			e1.printStackTrace();
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		return createResponse(e, response);

	}


	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(1000, "Not Implemented!"));
	}
	
	//TODO: Move to smart-commons
	private List<Product> createResponse(Product p, GetBalanceAndDateResponse response) {
		
		LOGGER.debug("Convering CS - IL response");
		List<Product> products = new ArrayList<Product>();
		
		Product product = new Product();
		product.setResourceName(p.getResourceName());
		product.setQuotaDefined(-1);
		product.setValidity(-1);
		
		// Start processing flattening of GetAccountDetailsResponse
		Map<String, String> balanceAndDateInfo = new HashMap<String, String>();
		
		// Dedicated Accounts...
		int index = 0;
		for (DedicatedAccountInformation daInformation: response.getDedicatedAccountInformation()) {
			String daInfo ="" + daInformation.getDedicatedAccountID() 
					+ "," + daInformation.getDedicatedAccountValue1()
					+ "," + daInformation.getDedicatedAccountValue2()
					+ "," + ((daInformation.getStartDate()==null)?"null":daInformation.getStartDate().getTime())
					+ "," + ((daInformation.getExpiryDate()==null)?"null":daInformation.getExpiryDate().getTime())
					+ "," + ((daInformation.getPamServiceID()==null)?"null":daInformation.getPamServiceID())
					+ "," + ((daInformation.getOfferID()==null)?"null":daInformation.getOfferID())
					+ "," + ((daInformation.getProductID()==null)?"null":daInformation.getProductID())
					+ "," + daInformation.isDedicatedAccountRealMoneyFlag()
					+ "," + ((daInformation.getClosestExpiryDate()==null)?"null":daInformation.getClosestExpiryDate().getTime())
					+ "," + ((daInformation.getClosestExpiryValue1()==null)?"null":daInformation.getClosestExpiryValue1())
					+ "," + ((daInformation.getClosestAccessibleDate()==null)?"null":daInformation.getClosestAccessibleDate())
					+ "," + ((daInformation.getClosestExpiryValue1()==null)?"null":daInformation.getClosestExpiryValue1())
					+ "," + ((daInformation.getClosestExpiryValue2()==null)?"null":daInformation.getClosestExpiryValue2())
					+ "," + ((daInformation.getDedicatedAccountActiveValue1()==null)?"null":daInformation.getDedicatedAccountActiveValue1())
					+ "," + ((daInformation.getDedicatedAccountUnitType()==null)?"null":daInformation.getDedicatedAccountUnitType())
					+ "," + daInformation.isCompositeDedicatedAccountFlag()	+ ":+:";
			
			if(daInformation.getSubDedicatedAccountInformation() != null) {
				String subDA = "";
				for (SubDedicatedInfo subDedicatedInfo: daInformation.getSubDedicatedAccountInformation()) {
					subDA += (subDA.isEmpty()?"":"|||");
					subDA += ((subDedicatedInfo.getDedicatedAccountValue1()==null)?"null":subDedicatedInfo.getDedicatedAccountValue1())
							+ "," + ((subDedicatedInfo.getDedicatedAccountValue2()==null)?"null":subDedicatedInfo.getDedicatedAccountValue2())
							+ "," + ((subDedicatedInfo.getStartDate()==null)?"null":subDedicatedInfo.getStartDate().getTime())
							+ "," + ((subDedicatedInfo.getExpiryDate()==null)?"null":subDedicatedInfo.getExpiryDate().getTime());
					daInfo += subDA; 
				}
			}
			balanceAndDateInfo.put("DA" + "." + ++index, daInfo); 
			
		}
			
			LOGGER.debug("Packed all dedicated accounts...");

		// offer info...
		index = 0;
		if(response.getOfferInformationList() != null) {
			
			for (OfferInformation offerInformation: response.getOfferInformationList()) {
				String offerInfo = offerInformation.getOfferID() 
						+ "," + ((offerInformation.getStartDate()==null)?"null":offerInformation.getStartDate().getTime())
						+ "," + ((offerInformation.getStartDateTime()==null)?"null":offerInformation.getStartDateTime().getTime())
						+ "," + ((offerInformation.getExpiryDate()==null)?"null":offerInformation.getExpiryDate().getTime())
						+ "," + ((offerInformation.getExpiryDateTime()==null)?"null":offerInformation.getExpiryDateTime().getTime());
				
				balanceAndDateInfo.put("OFFER_INFO" + "." + ++index, "" + offerInfo);
				
			}
		}
		LOGGER.debug("Packed all offer info...");
		
		
		product.setMetas(balanceAndDateInfo);
		products.add(product);
		return products;
	}




	@Override
	public String toString() {
		return "ReadBalancesProfile []";
	}

}

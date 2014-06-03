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

	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_ID	= "READ_BALANCES_DEDICATED_ACCOUNT_ID";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1	= "READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_VALUE_2	= "READ_BALANCES_DEDICATED_ACCOUNT_VALUE_2";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_EXPIRY_DATE	= "READ_BALANCES_DEDICATED_ACCOUNT_EXPIRY_DATE";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_START_DATE	= "READ_BALANCES_DEDICATED_ACCOUNT_START_DATE";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_PAM_SERVICE_ID	= "READ_BALANCES_DEDICATED_ACCOUNT_PAM_SERVICE_ID";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_OFFER_ID	= "READ_BALANCES_DEDICATED_ACCOUNT_OFFER_ID";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_PRODUCT_ID	= "READ_BALANCES_DEDICATED_ACCOUNT_PRODUCT_ID";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_REAL_MONEY_FLAG	= "READ_BALANCES_DEDICATED_ACCOUNT_REAL_MONEY_FLAG";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_DATE	= "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_DATE";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_1	= "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_1";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_2	= "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_2";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_DATE	= "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_DATE";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_1	= "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_1";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_2	= "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_2";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_1	= "READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_1";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_2	= "READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_2";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_START_DATE	= "READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_START_DATE";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_EXPIRY_DATE	= "READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_EXPIRY_DATE";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_1	= "READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_1";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_2	= "READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_2";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_UNIT_TYPE	= "READ_BALANCES_DEDICATED_ACCOUNT_UNIT_TYPE";
	private static final String	READ_BALANCES_DEDICATED_ACCOUNT_COMPOSITE_DA_FLAG	= "READ_BALANCES_DEDICATED_ACCOUNT_COMPOSITE_DA_FLAG";
	private static final String READ_BALANCES_OFFER_INFO_OFFER_ID = "READ_BALANCES_OFFER_ID";
	private static final String READ_BALANCES_OFFER_INFO_START_DATE = "READ_BALANCES_START_DATE";
	private static final String READ_BALANCES_OFFER_INFO_EXPIRY_DATE = "READ_BALANCES_EXPIRY_DATE";
	private static final String READ_BALANCES_OFFER_INFO_START_DATE_TIME = "READ_BALANCES_START_DATE_TIME";
	private static final String READ_BALANCES_OFFER_INFO_EXPIRY_DATE_TIME = "READ_BALANCES_EXPIRY_DATE_TIME";

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
		String subscriberId = map.get("SUSBCRIBER_ID");
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
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_ID + "." + ++index, "" + daInformation.getDedicatedAccountID());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1 + "." + index, "" + daInformation.getDedicatedAccountValue1());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_VALUE_2 + "." + index, "" + daInformation.getDedicatedAccountValue2());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_EXPIRY_DATE + "." + index, "" + daInformation.getExpiryDate().getTime());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_START_DATE + "." + index, "" + daInformation.getStartDate().getTime());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_PAM_SERVICE_ID + "." + index, "" + daInformation.getPamServiceID());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_OFFER_ID + "." + index, "" + daInformation.getOfferID());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_PRODUCT_ID + "." + index, "" + daInformation.getProductID());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_REAL_MONEY_FLAG + "." + index, "" + daInformation.isDedicatedAccountRealMoneyFlag());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_DATE + "." + index, "" + daInformation.getClosestExpiryDate().getTime());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_1 + "." + index, "" + daInformation.getClosestExpiryValue1());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_2 + "." + index, "" + daInformation.getClosestExpiryValue2());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_DATE + "." + index, "" + daInformation.getClosestAccessibleDate());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_1 + "." + index, "" + daInformation.getClosestExpiryValue1());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_2 + "." + index, "" + daInformation.getClosestExpiryValue2());
			
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_1 + "." + index, "" + daInformation.getDedicatedAccountActiveValue1());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_2 + "." + index, "" + daInformation.getDedicatedAccountActiveValue2());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_UNIT_TYPE + "." + index, "" + daInformation.getDedicatedAccountUnitType());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_COMPOSITE_DA_FLAG + "." + index, "" + daInformation.isCompositeDedicatedAccountFlag());
			
			int subindex = 0;
			for (SubDedicatedInfo subDedicatedInfo: daInformation.getSubDedicatedAccountInformation()) {
				balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_1 + "." + ++subindex, "" + subDedicatedInfo.getDedicatedAccountValue1());
				balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_2 + "." + subindex, "" + subDedicatedInfo.getDedicatedAccountValue2());
				balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_START_DATE + "." + subindex, "" + subDedicatedInfo.getStartDate().getTime());
				balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_EXPIRY_DATE + "." + subindex, "" + subDedicatedInfo.getExpiryDate().getTime());
			}
		}
		LOGGER.debug("Packed all dedicated accounts...");

		// offer info...
		index = 0;
		for (OfferInformation offerInformation: response.getOfferInformationList()) {
			balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_OFFER_ID + "." + ++index, "" + offerInformation.getOfferID());
			balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_START_DATE + "." + index, "" + offerInformation.getStartDate().getTime());
			balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_START_DATE_TIME + "." + index, "" + offerInformation.getStartDateTime().getTime());
			balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_EXPIRY_DATE + "." + index, "" + offerInformation.getExpiryDate().getTime());
			balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_EXPIRY_DATE_TIME + "." + index, "" + offerInformation.getExpiryDateTime().getTime());
		}
		LOGGER.debug("Packed all offer info...");
		
		
		
		product.setMetas(balanceAndDateInfo);
		products.add(product);
		return products;
	}
}

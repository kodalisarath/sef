package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetAccountDetailsCommand;
import com.ericsson.raso.sef.client.air.command.GetBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.request.GetAccountDetailsRequest;
import com.ericsson.raso.sef.client.air.request.GetBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.AccountFlags;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.GetAccountDetailsResponse;
import com.ericsson.raso.sef.client.air.response.GetBalanceAndDateResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.SubDedicatedInfo;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class EntireReadSubscriberProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 2168287678631825571L;
	private static final Logger LOGGER = LoggerFactory.getLogger(EntireReadSubscriberProfile.class);

	//Get Account Details...
	private static final String READ_SUBSCRIBER_ACTIVATION_DATE = "READ_SUBSCRIBER_ACTIVATION_DATE";
	private static final String READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE = "READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE = "READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE";
	private static final String READ_SUBSCRIBER_SERVICE_OFFERING_ID = "READ_SUBSCRIBER_SERVICE_OFFERING_ID";
	private static final String READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG = "READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG";
	private static final String READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG = "READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG";
	private static final String READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG = "READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG";
	private static final String READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG = "READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG = "READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG";
	private static final String READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG = "READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG = "READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG";
	private static final String READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG = "READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG";
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER_ID = "READ_SUBSCRIBER_OFFER_ID";
	private static final String READ_SUBSCRIBER_OFFER_INFO_START_DATE = "READ_SUBSCRIBER_START_DATE";
	private static final String READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE = "READ_SUBSCRIBER_EXPIRY_DATE";
	private static final String READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME = "READ_SUBSCRIBER_START_DATE_TIME";
	private static final String READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME = "READ_SUBSCRIBER_EXPIRY_DATE_TIME";

	//Get Balance & Dates...
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


	public EntireReadSubscriberProfile(String name) {
		super(name);
	}



	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(6000, "Not Implemented!"));
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(6000, "Not Implemented!"));
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		LOGGER.debug("Request for Get Account Details...");

		HashMap<String, String> details = new HashMap<String, String>();

		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));

		GetAccountDetailsRequest accountDetailsRequest = new GetAccountDetailsRequest();
		String subscriberId = map.get("SUBSCRIBER_ID");
		if (subscriberId == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'SUBSCRIBER_ID' missing in request!!"));
		accountDetailsRequest.setSubscriberNumber(subscriberId);
		accountDetailsRequest.setSubscriberNumberNAI(1);

		GetAccountDetailsResponse accountDetailsResponse = null;
		GetAccountDetailsCommand accountDetailsCommand = new GetAccountDetailsCommand(accountDetailsRequest);

		try {
			accountDetailsResponse = accountDetailsCommand.execute();
		} catch (SmException e1) {
			e1.printStackTrace();
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}

		this.processAccountDetailsResponse(details, accountDetailsResponse);

		LOGGER.debug("Query request for read balances...");


		GetBalanceAndDateRequest balanceAndDateRequest = new GetBalanceAndDateRequest();
		balanceAndDateRequest.setSubscriberNumber(subscriberId);
		balanceAndDateRequest.setSubscriberNumberNAI(1);

		GetBalanceAndDateResponse balanceAndDateResponse = null;
		GetBalanceAndDateCommand balanceAndDateCommand = new GetBalanceAndDateCommand(balanceAndDateRequest);

		try {
			balanceAndDateResponse = balanceAndDateCommand.execute();
		} catch (SmException e1) {
			e1.printStackTrace();
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}

		this.processBalanceAndDateResponse(details, balanceAndDateResponse);

		List<Product> products = new ArrayList<Product>();
		Product product = new Product();
		product.setResourceName(e.getResourceName());
		product.setQuotaDefined(-1);
		product.setValidity(-1);product.setMetas(details);
		products.add(product);
		return products;


	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(6000, "Not Implemented!"));
	}

	private void processAccountDetailsResponse(HashMap<String, String> accountDetails, GetAccountDetailsResponse response) {
		// direct attributes...
		if (response.getActivationDate() != null)
			accountDetails.put(READ_SUBSCRIBER_ACTIVATION_DATE, "" + response.getActivationDate().getTime());

		if (response.getSupervisionExpiryDate() != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE, "" + response.getSupervisionExpiryDate().getTime());

		if (response.getServiceFeeExpiryDate() != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE, "" + response.getServiceFeeExpiryDate().getTime());

		LOGGER.debug("Packed all date attributes...");

		// service offerings
		int index = 0;
		for (com.ericsson.raso.sef.client.air.response.ServiceOffering serviceOffering: response.getServiceOfferings()) {
			accountDetails.put(READ_SUBSCRIBER_SERVICE_OFFERING_ID + "." + ++index, "" + serviceOffering.getServiceOfferingID());
			accountDetails.put(READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG + "." + index, "" + serviceOffering.isServiceOfferingActiveFlag());
		}
		LOGGER.debug("Packed all service offerings...");

		// account flags
		AccountFlags accountFlags = response.getAccountFlags();
		Boolean flag = accountFlags.isActivationStatusFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG, "" + flag);

		flag = accountFlags.isNegativeBarringStatusFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG, "" + flag);

		flag = accountFlags.isSupervisionPeriodWarningActiveFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG, "" + flag);

		flag = accountFlags.isServiceFeePeriodWarningActiveFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG, "" + flag);

		flag = accountFlags.isSupervisionPeriodExpiryFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG, "" + flag);

		flag = accountFlags.isServiceFeePeriodExpiryFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG, "" + flag);

		flag = accountFlags.isTwoStepActivationFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG, "" + flag);

		LOGGER.debug("Packed all account flags...");

		// offer info...
		index = 0;
		for (OfferInformation offerInformation: response.getOfferInformationList()) {
			if(offerInformation != null) {
				accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_OFFER_ID + "." + ++index, "" + offerInformation.getOfferID());

				if(offerInformation.getStartDate() != null)
					accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_START_DATE + "." + index, "" + offerInformation.getStartDate().getTime());

				if(offerInformation.getStartDateTime() != null)
					accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME + "." + index, "" + offerInformation.getStartDateTime().getTime());

				if(offerInformation.getExpiryDate() != null)
					accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE + "." + index, "" + offerInformation.getExpiryDate().getTime());

				if(offerInformation.getExpiryDateTime() != null)
					accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME + "." + index, "" + offerInformation.getExpiryDateTime().getTime());
			}
		}
		LOGGER.debug("Packed all offer info..." + accountDetails.toString());

	}


	private void processBalanceAndDateResponse(HashMap<String, String> balanceAndDateInfo, GetBalanceAndDateResponse response) {
		// Dedicated Accounts...
		int index = 0;
		for (DedicatedAccountInformation daInformation: response.getDedicatedAccountInformation()) {
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_ID + "." + ++index, "" + daInformation.getDedicatedAccountID());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1 + "." + index, "" + daInformation.getDedicatedAccountValue1());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_VALUE_2 + "." + index, "" + daInformation.getDedicatedAccountValue2());

			if(daInformation.getExpiryDate() != null)
				balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_EXPIRY_DATE + "." + index, "" + daInformation.getExpiryDate().getTime());

			if(daInformation.getStartDate() != null)
				balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_START_DATE + "." + index, "" + daInformation.getStartDate().getTime());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_PAM_SERVICE_ID + "." + index, "" + daInformation.getPamServiceID());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_OFFER_ID + "." + index, "" + daInformation.getOfferID());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_PRODUCT_ID + "." + index, "" + daInformation.getProductID());
			balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_REAL_MONEY_FLAG + "." + index, "" + daInformation.isDedicatedAccountRealMoneyFlag());

			if(daInformation.getClosestExpiryDate() != null)
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
			if(daInformation.getSubDedicatedAccountInformation() != null) {
				for (SubDedicatedInfo subDedicatedInfo: daInformation.getSubDedicatedAccountInformation()) {
					if(subDedicatedInfo != null) {
						if(subDedicatedInfo.getDedicatedAccountValue1() != null)
							balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_1 + "." + index+ "." + ++subindex, "" + subDedicatedInfo.getDedicatedAccountValue1());

						if(subDedicatedInfo.getDedicatedAccountValue2() != null)
							balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_2 + "." + index+ "." + subindex, "" + subDedicatedInfo.getDedicatedAccountValue2());

						if(subDedicatedInfo.getStartDate() != null)
							balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_START_DATE + "." + index+ "." +  subindex, "" + subDedicatedInfo.getStartDate().getTime());

						if(subDedicatedInfo.getExpiryDate() != null)
							balanceAndDateInfo.put(READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_EXPIRY_DATE + "." + index+ "." +  subindex, "" + subDedicatedInfo.getExpiryDate().getTime());
					}
				}
			}
		}
		LOGGER.debug("Packed all dedicated accounts...");

		// offer info...
		index = 0;
		for (OfferInformation offerInformation: response.getOfferInformationList()) {
			if(offerInformation != null) {
				balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_OFFER_ID + "." + ++index, "" + offerInformation.getOfferID());

				if(offerInformation.getStartDate() != null)
					balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_START_DATE + "." + index, "" + offerInformation.getStartDate().getTime());

				if(offerInformation.getStartDateTime() != null)
					balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_START_DATE_TIME + "." + index, "" + offerInformation.getStartDateTime().getTime());

				if(offerInformation.getExpiryDate() != null)
					balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_EXPIRY_DATE + "." + index, "" + offerInformation.getExpiryDate().getTime());

				if(offerInformation.getExpiryDateTime() != null)
					balanceAndDateInfo.put(READ_BALANCES_OFFER_INFO_EXPIRY_DATE_TIME + "." + index, "" + offerInformation.getExpiryDateTime().getTime());
			}
		}
		LOGGER.debug("Packed all offer info...");

	}



	@Override
	public String toString() {
		return "EntireReadSubscriberProfile []";
	}

}

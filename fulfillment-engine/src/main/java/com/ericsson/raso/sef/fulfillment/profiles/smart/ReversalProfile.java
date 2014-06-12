package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.command.UpdateBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.request.DedicatedAccountUpdateInformation;
import com.ericsson.raso.sef.client.air.request.GetBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.request.UpdateBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.GetBalanceAndDateResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.UpdateBalanceAndDateResponse;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class ReversalProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 3097196531251485097L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReversalProfile.class);
	
	private static final String DA_ID = null;
	private static final String DA_VALUE_1 = null;
	private static final String DA_UNIT_TYPE = null; 

	private String transactionCurrency;
	private Date serviceFeeExpiryDate;
	private Date supervisionExpiryDate;
	private String transactionType;
	private String transactionCode;
	private List<DedicatedAccountUpdateInformation> dedicatedAccountUpdateInformation;
	private List<DedicatedAccountReversal> daReversals = null;
	private List<TimerOfferReversal> toReversals = null;
	

	protected ReversalProfile(String name) {
		super(name);
	}

	@Override
	public List<Product> fulfill(Product p, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		String msisdn = map.get("msisdn");
		String externalData1 = map.get("chargeCode");
		String externalData2 = map.get("eventInfo");

		
		// Get Balance & Date...
		GetBalanceAndDateRequest balanceAndDateRequest = new GetBalanceAndDateRequest();
		balanceAndDateRequest.setSubscriberNumber(msisdn);
		balanceAndDateRequest.setSubscriberNumberNAI(1);
		GetBalanceAndDateCommand balanceAndDateCommand = new GetBalanceAndDateCommand(balanceAndDateRequest);
		GetBalanceAndDateResponse balanceAndDateResponse = null;
		
		try {
			balanceAndDateResponse = balanceAndDateCommand.execute();
		} catch (SmException e) {
			LOGGER.error("Failed GetBalance&Date. Cause: " + e.getMessage(), e);
			throw new FulfillmentException(e.getComponent(), e.getStatusCode());
		}
		
		// Get longest and second longest dates....
		long daLongestDate = 0;
		long daSecondLongestDate = 0;
		for (DedicatedAccountInformation daInfo: balanceAndDateResponse.getDedicatedAccountInformation()) {
			long expiryDate = daInfo.getExpiryDate().getTime();
			if (expiryDate > daSecondLongestDate) {
				daLongestDate = daSecondLongestDate;
				daSecondLongestDate = expiryDate;
				if (daLongestDate == 0)
					daLongestDate = expiryDate;
			}
		}
		
		long toLongestDate = 0;
		long toSecondLongestDate = 0;
		String timerOffers = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "timerOffers");
		for (OfferInformation offerInformation: balanceAndDateResponse.getOfferInformationList()) {
			String offerId = "" + offerInformation.getOfferID();
			if (timerOffers.contains(offerId)) {
				long expiryDate = offerInformation.getExpiryDate().getTime();
				if (expiryDate > toSecondLongestDate) {
					toLongestDate = toSecondLongestDate;
					toSecondLongestDate = expiryDate;
					if (toLongestDate == 0)
						toLongestDate = expiryDate;
				}
			}
				
		}
		
		// Now... calculate the balance & dates to reverse
		for (DedicatedAccountUpdateInformation daInfo: this.dedicatedAccountUpdateInformation) {
			
		}
		
		
		// Now... send the reversal
		UpdateBalanceAndDateRequest request = new UpdateBalanceAndDateRequest();
		request.setDedicatedAccountUpdateInformation(dedicatedAccountUpdateInformation);
		request.setExternalData1(externalData1);
		request.setExternalData2(externalData2);
		request.setSubscriberNumber(msisdn);
		request.setSubscriberNumberNAI(1);
		
		UpdateBalanceAndDateCommand command = new UpdateBalanceAndDateCommand(request);
		UpdateBalanceAndDateResponse response = null;
		try {
			response = command.execute();
		} catch (SmException e) {
			LOGGER.error("Failed BalanceAdjustment. Cause: " + e.getMessage(), e);
			throw new FulfillmentException(e.getComponent(), e.getStatusCode());
		}
		
		//Now...check if the 
		
		
		products.add(p);
		
		
		return products;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		return products;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		return products;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		return products;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public Date getServiceFeeExpiryDate() {
		return serviceFeeExpiryDate;
	}

	public void setServiceFeeExpiryDate(Date serviceFeeExpiryDate) {
		this.serviceFeeExpiryDate = serviceFeeExpiryDate;
	}

	public Date getSupervisionExpiryDate() {
		return supervisionExpiryDate;
	}

	public void setSupervisionExpiryDate(Date supervisionExpiryDate) {
		this.supervisionExpiryDate = supervisionExpiryDate;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public List<DedicatedAccountUpdateInformation> getDedicatedAccountUpdateInformation() {
		return dedicatedAccountUpdateInformation;
	}

	public void setDedicatedAccountUpdateInformation(List<DedicatedAccountUpdateInformation> dedicatedAccountUpdateInformation) {
		this.dedicatedAccountUpdateInformation = dedicatedAccountUpdateInformation;
	}

	@Override
	public String toString() {
		return null;
	}

}

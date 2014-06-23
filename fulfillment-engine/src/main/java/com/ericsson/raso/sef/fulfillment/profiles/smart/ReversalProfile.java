package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.command.UpdateBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.command.UpdateOfferCommand;
import com.ericsson.raso.sef.client.air.request.DedicatedAccountUpdateInformation;
import com.ericsson.raso.sef.client.air.request.GetBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.request.UpdateBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.request.UpdateOfferRequest;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountChangeInformation;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.GetBalanceAndDateResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.UpdateBalanceAndDateResponse;
import com.ericsson.raso.sef.client.air.response.UpdateOfferResponse;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class ReversalProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 3097196531251485097L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReversalProfile.class);
	
	private static final String REVERSAL_DEDICATED_ACCOUNT_ID = "REVERSAL_DEDICATED_ACCOUNT_ID";
	private static final String REVERSAL_DEDICATED_ACCOUNT_NEW_VALUE = "REVERSAL_BALANCES_DEDICATED_ACCOUNT_NEW_VALUE";
	private static final String REVERSAL_DEDICATED_ACCOUNT_REVERSED_AMOUNT = "REVERSAL_DEDICATED_ACCOUNT_REVERSED_AMOUNT";
	private static final String REVERSAL_OFFER_ID = "REVERSAL_OFFER_ID";
	private static final String REVERSAL_OFFER_EXPIRY = "REVERSAL_OFFER_EXPIRY";
	
	private String transactionCurrency;
	private Date serviceFeeExpiryDate;
	private Date supervisionExpiryDate;
	private String transactionType;
	private List<DedicatedAccountReversal> daReversals = null;
	private List<TimerOfferReversal> toReversals = null;
	

	public ReversalProfile(String name) {
		super(name);
	}

	@Override
	public List<Product> fulfill(Product p, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		
		LOGGER.debug("Confirm metas: " + map);
		String msisdn = map.get("msisdn");
		if (msisdn == null) {
			LOGGER.debug("'msisdn' key was null... trying 'SUBSCRIBER_ID'");
			msisdn = map.get("SUBSCRIBER_ID");
		}
		String externalData1 = map.get("eventName");
		String externalData2 = map.get("eventInfo");
		String channel = map.get("channelName");

		
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
			if (expiryDate > daLongestDate) {
				daLongestDate = daSecondLongestDate;
				daSecondLongestDate = expiryDate;
				if (daLongestDate == 0)
					daLongestDate = expiryDate;
			}
		}
		
		long toLongestDate = 0;
		long toSecondLongestDate = 0;
		int longestExpiringOffer = 0;
		for (OfferInformation offerInformation: balanceAndDateResponse.getOfferInformationList()) {
			String offerId = "" + offerInformation.getOfferID();
			String relatedDA = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", offerId);
			if (relatedDA != null) {
				long expiryDate = ((offerInformation.getExpiryDate() != null)?offerInformation.getExpiryDate().getTime():offerInformation.getExpiryDateTime().getTime());
				if(expiryDate > toSecondLongestDate) {
					if (expiryDate > toLongestDate) {
						toSecondLongestDate = toLongestDate;
						toLongestDate = expiryDate;
						longestExpiringOffer = offerInformation.getOfferID();
					} else {
						toSecondLongestDate = expiryDate;
					}
				}
				
			}
		}
		LOGGER.debug("Longest Date: " + toLongestDate + ", Second Longest Date: " + toSecondLongestDate);
		
		// Now... calculate the balance & dates to reverse
		long supervisionPeriodExpiryDate = toLongestDate;
		long serviceFeeExpiryDate = toLongestDate;
		LOGGER.debug("supervision: " + supervisionPeriodExpiryDate + ", service fee: " + serviceFeeExpiryDate);
		long newExpiryDate = 0;
		
		List<OfferInformation> offersToUpdate = new ArrayList<OfferInformation>();
		List<DedicatedAccountUpdateInformation> dasToUpdate = new ArrayList<DedicatedAccountUpdateInformation>();
		
		for (TimerOfferReversal toReversal: toReversals) {
			OfferInformation impactedOffer = this.getImpactedOffer(toReversal.getOfferID(), balanceAndDateResponse.getOfferInformationList());
			if (impactedOffer == null) {
				LOGGER.error("Offer ID (" + toReversal.getOfferID() + ") in Reversal BizConfig was not found in Subscriber Profile");
				throw new FulfillmentException("ffe", new ResponseCode(999, "Technical Error"));
			}
			
			long impactedExpiry = ((impactedOffer.getExpiryDate() != null)?impactedOffer.getExpiryDate().getTime():impactedOffer.getExpiryDateTime().getTime());
			LOGGER.debug("From Biz Config: " + toReversal.hoursToReverse + ", hours getter form: " + toReversal.getHoursToReverse());
			newExpiryDate = impactedExpiry - (toReversal.hoursToReverse);
			LOGGER.debug("New Expiry Calculated: " + newExpiryDate + ", date form: " + new Date(newExpiryDate));
			LOGGER.debug("Check impactedExpiry: " + new Date(impactedExpiry) + " with longestDate: " + new Date(toLongestDate));
			
			if (impactedOffer.getOfferID().compareTo(longestExpiringOffer)==0) {
				if (newExpiryDate < toLongestDate) {
					if (newExpiryDate < toSecondLongestDate) {
						supervisionPeriodExpiryDate = toSecondLongestDate;
						serviceFeeExpiryDate = toSecondLongestDate;
						LOGGER.debug("new supervision: " + new Date(supervisionPeriodExpiryDate) + ", servicefee: " + new Date(serviceFeeExpiryDate));
					} else {
						supervisionPeriodExpiryDate = newExpiryDate;
						serviceFeeExpiryDate = newExpiryDate;
						LOGGER.debug("new supervision: " + new Date(supervisionPeriodExpiryDate) + ", servicefee: " + new Date(serviceFeeExpiryDate));
					}
				}
			}
			
			// now do the reversal of all other impacted offers...
			///impactedOffer.setExpiryDate(new Date(newExpiryDate));
			impactedOffer.setExpiryDateTime(new Date(newExpiryDate));
			///impactedOffer.setStartDate(new Date(impactedExpiry - toReversal.hoursToReverse));
			//impactedOffer.setStartDateTime(new Date(impactedOffer.getStartDateTime().getTime() - (long)(toReversal.hoursToReverse)));

			offersToUpdate.add(impactedOffer);

			DedicatedAccountInformation impactedDA =  this.getImpactedDA(toReversal.dedicatedAccountInformationID, balanceAndDateResponse.getDedicatedAccountInformation());
			DedicatedAccountReversal daReversal = this.getRelevantReveralDA(toReversal.dedicatedAccountInformationID);
			DedicatedAccountUpdateInformation daToUpdate = new DedicatedAccountUpdateInformation();
			daToUpdate.setDedicatedAccountID(impactedDA.getDedicatedAccountID());
			daToUpdate.setDedicatedAccountUnitType(impactedDA.getDedicatedAccountUnitType());
			daToUpdate.setAdjustmentAmountRelative("-" + this.getAmountToReverse(impactedDA.getDedicatedAccountID()));
			daToUpdate.setExpiryDate(new Date(newExpiryDate));
			dasToUpdate.add(daToUpdate);
			LOGGER.debug("Adding DA to update in the AIR UCIP Request: " + daToUpdate);
				
		}
		
		
		// Now... send the reversal on DAs....
		
		UpdateBalanceAndDateRequest request = new UpdateBalanceAndDateRequest();
		request.setDedicatedAccountUpdateInformation(dasToUpdate);
		request.setExternalData1(externalData1);
		request.setExternalData2(externalData2);
		request.setSubscriberNumber(msisdn);
		request.setSubscriberNumberNAI(1);
		//request.setTransactionType(channel);
		request.setTransactionCurrency(this.transactionCurrency);
		//request.setTransactionCode(externalData1);
		
		if (supervisionPeriodExpiryDate < toLongestDate)
			request.setSupervisionExpiryDate(new Date(supervisionPeriodExpiryDate));
		
		if (serviceFeeExpiryDate < toLongestDate)
			request.setServiceFeeExpiryDate(new Date(serviceFeeExpiryDate));
		
		UpdateBalanceAndDateCommand updateBalanceAndDateCommand = new UpdateBalanceAndDateCommand(request);
		UpdateBalanceAndDateResponse updateBalanceAndDateResponse = null;
		try {
			updateBalanceAndDateResponse = updateBalanceAndDateCommand.execute();
		} catch (SmException e) {
			LOGGER.error("Failed RefillReveral - DA Update step. Cause: " + e.getMessage(), e);
			throw new FulfillmentException(e.getComponent(), e.getStatusCode());
		}

		
		//Now...send the reversal on DAs....
		Map<String, String> responseMetas = new HashMap<String, String>();
		int index = 0;
		for (OfferInformation updatedOffer: offersToUpdate) {

			UpdateOfferRequest updateOfferRequest = new UpdateOfferRequest();
			//updateOfferRequest.setExpiryDate(updatedOffer.getExpiryDate());
			updateOfferRequest.setExpiryDateTime(new Date(newExpiryDate));
			//updateOfferRequest.setStartDateTime(updatedOffer.getStartDate());
			//updateOfferRequest.setStartDateTime(updatedOffer.getStartDateTime());
			updateOfferRequest.setOfferID(updatedOffer.getOfferID());
			updateOfferRequest.setOfferType(2);
			updateOfferRequest.setSubscriberNumber(msisdn);
			updateOfferRequest.setSubscriberNumberNAI(1);
			
			UpdateOfferCommand updateOfferCommand = new UpdateOfferCommand(updateOfferRequest);
			UpdateOfferResponse updateOfferResponse = null;
			try {
				updateOfferResponse = updateOfferCommand.execute();
			} catch (SmException e) {
				LOGGER.error("Failed RefillReveral - Timer Offer Update step. Cause: " + e.getMessage(), e);
				throw new FulfillmentException(e.getComponent(), e.getStatusCode());
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String reversalEntry = "" + updatedOffer.getOfferID() + "," + format.format(updatedOffer.getExpiryDateTime());
			Integer associatedDaId = Integer.parseInt(SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", "" + updateOfferRequest.getOfferID()));
			LOGGER.debug("associatedDA ID: " + associatedDaId);
			for (DedicatedAccountChangeInformation daResultInfo: updateBalanceAndDateResponse.getDedicatedAccountInformation()) {
				if (daResultInfo.getDedicatedAccountID().compareTo(associatedDaId)==0) {
					reversalEntry += "," + associatedDaId 
							+ "," + daResultInfo.getDedicatedAccountValue1() 
							+ "," + this.getReversalDA(associatedDaId).getAmountToReverse();
				}
			}
			
			responseMetas.put(REVERSAL_OFFER_ID + "." + ++index, reversalEntry);
					 
		}
			
	

		
		p.setMetas(responseMetas);
		products.add(p);
		
		
		return products;
	}
	
	

	private long getTimeToReverse(Integer dedicatedAccountID) {
		for (DedicatedAccountReversal daReversal: this.daReversals) {
			if (daReversal.dedicatedAccountInformationID.compareTo(dedicatedAccountID)==0)
				return daReversal.hoursToReverse;
		}
		return 0;
	}

	private long getAmountToReverse(Integer dedicatedAccountID) {
		for (DedicatedAccountReversal daReversal: this.daReversals) {
			if (daReversal.dedicatedAccountInformationID.compareTo(dedicatedAccountID)==0)
				return daReversal.amountToReverse;
		}
		return 0;
	}

	private DedicatedAccountReversal getReversalDA(Integer associatedDaId) {
		for (DedicatedAccountReversal reversal: this.daReversals) {
			if (reversal.dedicatedAccountInformationID.compareTo(associatedDaId) == 0) {
				return reversal;
			}
			
		}
		return null;
	}

	private DedicatedAccountReversal getRelevantReveralDA(int daID) {
		for (DedicatedAccountReversal da: this.daReversals) {
			if (da.getDedicatedAccountInformationID().compareTo(daID) == 0)
				return da;
		}
		return null;
	}

	private OfferInformation getImpactedOffer(Integer offerID, List<OfferInformation> offerInformationList) {
		if (offerInformationList != null) {
			for (OfferInformation offerInfo: offerInformationList) {
				if (offerInfo.getOfferID().compareTo(offerID) == 0) {
					return offerInfo;
				}
			}
		}
		return null;
	}

	private DedicatedAccountInformation getImpactedDA(Integer dedicatedAccountInformationID, List<DedicatedAccountInformation> dedicatedAccountInformationList) {
		if (dedicatedAccountInformationList != null) {
			for (DedicatedAccountInformation daInfo: dedicatedAccountInformationList) {
				if (dedicatedAccountInformationID.compareTo(daInfo.getDedicatedAccountID()) == 0) {
					return daInfo;
				}
			}
		}
		return null;
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

	public List<DedicatedAccountReversal> getDaReversals() {
		return daReversals;
	}

	public void setDaReversals(List<DedicatedAccountReversal> daReversals) {
		this.daReversals = daReversals;
	}

	public List<TimerOfferReversal> getToReversals() {
		return toReversals;
	}

	public void setToReversals(List<TimerOfferReversal> toReversals) {
		this.toReversals = toReversals;
	}

	@Override
	public String toString() {
		return "ReversalProfile [transactionCurrency=" + transactionCurrency + ", transactionType=" + transactionType + ", daReversals="
				+ daReversals + ", toReversals=" + toReversals + "]";
	}

	

}

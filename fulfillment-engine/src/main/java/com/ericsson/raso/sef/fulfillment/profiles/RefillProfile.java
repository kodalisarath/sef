package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.RefillCommand;
import com.ericsson.raso.sef.client.air.request.RefillRequest;
import com.ericsson.raso.sef.client.air.response.AccBefAndAfterRef;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.RefillResponse;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class RefillProfile extends BlockingFulfillment<Product> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5706705833688931767L;
	private String refillProfileId;

	private Integer refillType;
	private String transactionAmount;
	private CurrencyCode transactionCurrency;

	private static final Logger LOGGER = LoggerFactory.getLogger(RefillProfile.class);
	
	public RefillProfile(String name) {
		super(name);
	}
	
	
	public String getRefillProfileId() {
		return refillProfileId;
	}

	public void setRefillProfileId(String refillProfileId) {
		this.refillProfileId = refillProfileId;
	}

	public Integer getRefillType() {
		return refillType;
	}

	public void setRefillType(Integer refillType) {
		this.refillType = refillType;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public CurrencyCode getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(CurrencyCode transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}


	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		
		LOGGER.debug("Fufill request for refill");

		RefillRequest refillRequest = new RefillRequest();
		refillRequest.setSubscriberNumber(map.get("msisdn"));
		refillRequest.setRefProfID(this.refillProfileId);
		refillRequest.setRefType(this.refillType);
		refillRequest.setTransacAmount(this.transactionAmount);
		refillRequest.setTransacCurrency(this.transactionCurrency.name());
		
		String extData1 = map.get(Constants.EX_DATA1);
		if(extData1 != null) {
			refillRequest.setExternalData1(extData1);
		}
		
		String extData2 = map.get(Constants.EX_DATA2);
		if(extData2 != null) {
			refillRequest.setExternalData2(extData2);
		}
		
		String extData3 = map.get(Constants.EX_DATA3);
		if(extData3 != null) {
			refillRequest.setExternalData3(extData3);
		}
		
		refillRequest.setRefAccBeforeFlag(true);
		refillRequest.setRefAccAfterFlag(true);
		
		RefillResponse response = null;
		RefillCommand command = new RefillCommand(refillRequest);
		
		try {
			response = command.execute();
		} catch (SmException e1) {
			LOGGER.error("Refill execution failed!!", e1);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		
		LOGGER.debug("Refill successful. Preparing response to return.");
		return createResponse(e, response);
	}


	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		LOGGER.debug("Preparing.......");
		List<Product> products = new ArrayList<Product>();
		return products;
	}


	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		List<Product> products = new ArrayList<Product>();
		return products;
	}


	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		List<Product> products = new ArrayList<Product>();
		return products;
	}
	
	//TODO: Move to smart-commons
	private List<Product> createResponse(Product p, RefillResponse response) {
		
		LOGGER.debug("Convering CS - IL response");
		List<Product> products = new ArrayList<Product>();
		
		//Fetch accounts before and after refill 
		AccBefAndAfterRef accBefore = response.getAccountBeforeRefill();
		AccBefAndAfterRef accAfter = response.getAccountAfterRefill();

		List<DedicatedAccountInformation> beforeDas = accBefore.getDedicatedAccInfo();
		List<DedicatedAccountInformation> afterDas = accAfter.getDedicatedAccInfo();

		Map<Integer, DedicatedAccountInformation> beforeDaMap = toDaMap(beforeDas);
		Map<Integer, DedicatedAccountInformation> afterDaMap = toDaMap(afterDas);

		// Fetch all accounts after refill
		List<OfferInformation> offerInformationList = accAfter.getOfferInformationList();

		Map<Integer, OfferInformation> offerMap = toOfferMap(offerInformationList);

		LOGGER.debug("Retrieved refill balance information");
		// For each CS offer Id associated to the refill prepare balance statement.. Product represent the recharged balance & validity
		if(this.getAbstractResources() != null) {
		for(String resource: this.getAbstractResources()) {
			
			Product product = new Product();
			product.setResourceName(resource);
			//Assumption here is all abstracted resource Id are Integers represented in string format. logic specific to SMART
			OfferInformation offerInformation = offerMap.get(Integer.parseInt(resource));
			
			if(offerInformation != null) {
				LOGGER.debug("Validity of offer: " + offerInformation.getExpiryDateTime());
			}
			
			//prepare validity
			if (offerInformation != null && offerInformation.getExpiryDateTime() != null) {
				product.setValidity(offerInformation.getExpiryDateTime().getTime());
			} else {
				product.setValidity(new Date(Long.MAX_VALUE).getTime());
			}
			
			// Fetch DA and prepare balances
			String daID = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_offerMapping", resource);
			if (daID == null) {
				LOGGER.debug("Associated DA not found.. continuing with next offer");
				continue;
			}
			
			LOGGER.debug("Proceeding with balance population");
			DedicatedAccountInformation beforeDa = beforeDaMap.get(Integer.valueOf(daID));
			DedicatedAccountInformation afterDa = afterDaMap.get(Integer.valueOf(daID));

			if (beforeDa != null) {
				if (beforeDa.getDedicatedAccountValue1() != null) {
					Long prevBalance = Long.valueOf(beforeDa.getDedicatedAccountValue1());
					if (beforeDa.getDedicatedAccountValue2() != null) {
						prevBalance += Long.valueOf(beforeDa.getDedicatedAccountValue2());
					}
					product.setQuotaDefined(prevBalance);
				}
			}
			if (afterDa != null) {
				if (afterDa.getDedicatedAccountValue1() != null) {
					Long currentBalance = Long.valueOf(afterDa.getDedicatedAccountValue1());
					if (afterDa.getDedicatedAccountValue2() != null) {
						currentBalance += Long.valueOf(afterDa.getDedicatedAccountValue2());
					}
					product.setQuotaConsumed(currentBalance);
				}
			}
			product.setName(p.getName());
			products.add(product);
		}
		} else {
			LOGGER.debug("No associated offers found. Empty response will be sent");
		}
		LOGGER.debug("Total products in response" + products.size());
		return products;
	}
	
	
	private Map<Integer, OfferInformation> toOfferMap(List<OfferInformation> list) {
		Map<Integer, OfferInformation> map = new LinkedHashMap<Integer, OfferInformation>();
		for (OfferInformation offer : list) {
			map.put(offer.getOfferID(), offer);
		}
		return map;
	}
	
	private Map<Integer, DedicatedAccountInformation> toDaMap(List<DedicatedAccountInformation> list) {
		Map<Integer, DedicatedAccountInformation> map = new LinkedHashMap<Integer, DedicatedAccountInformation>();
		for (DedicatedAccountInformation da : list) {
			map.put(da.getDedicatedAccountID(), da);
		}
		return map;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((refillProfileId == null) ? 0 : refillProfileId.hashCode());
		result = prime * result
				+ ((refillType == null) ? 0 : refillType.hashCode());
		result = prime
				* result
				+ ((transactionAmount == null) ? 0 : transactionAmount
						.hashCode());
		result = prime
				* result
				+ ((transactionCurrency == null) ? 0 : transactionCurrency
						.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefillProfile other = (RefillProfile) obj;
		if (refillProfileId == null) {
			if (other.refillProfileId != null)
				return false;
		} else if (!refillProfileId.equals(other.refillProfileId))
			return false;
		if (refillType == null) {
			if (other.refillType != null)
				return false;
		} else if (!refillType.equals(other.refillType))
			return false;
		if (transactionAmount == null) {
			if (other.transactionAmount != null)
				return false;
		} else if (!transactionAmount.equals(other.transactionAmount))
			return false;
		if (transactionCurrency != other.transactionCurrency)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "RefillProfile [refillProfileId=" + refillProfileId
				+ ", refillType=" + refillType + ", transactionAmount="
				+ transactionAmount + ", transactionCurrency="
				+ transactionCurrency + "]";
	}

}

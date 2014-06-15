package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GetAccountDetailsResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;

	private Date activationDate;
	private Date supervisionExpiryDate;
	private Date serviceFeeExpiryDate;

	private List<ServiceOffering> serviceOfferings;
	private AccountFlags accountFlags;
	private List<OfferInformation> offerInformationList;

	@SuppressWarnings("unchecked")
	public List<ServiceOffering> getServiceOfferings() {
		if (serviceOfferings == null) {
			Object[] serviceOfferingsRes = (Object[]) (((Map<?, ?>) result).get("serviceOfferings"));
			serviceOfferings = new ArrayList<ServiceOffering>();
			for (Object so : serviceOfferingsRes) {
				serviceOfferings.add(new ServiceOffering((Map<String, Object>) so));
			}
		}
		return serviceOfferings;
	}

	@SuppressWarnings("unchecked")
	public AccountFlags getAccountFlags() {
		if (accountFlags == null) {
			Object obj = (Object) (((Map<?, ?>) result).get("accountFlags"));
			accountFlags = new AccountFlags((Map<String, Object>) obj);
		}
		return accountFlags;
	}

	public Date getActivationDate() {
		if(activationDate == null) {
			activationDate = (Date) (((Map<?, ?>) result).get("activationDate"));
		}
		return activationDate;
	}

	public Date getSupervisionExpiryDate() {
		if(supervisionExpiryDate == null) {
			supervisionExpiryDate = (Date) (((Map<?, ?>) result).get("supervisionExpiryDate"));
		}
		return supervisionExpiryDate;
	}

	public Date getServiceFeeExpiryDate() {
		if(serviceFeeExpiryDate == null) {
			serviceFeeExpiryDate = (Date) (((Map<?, ?>) result).get("serviceFeeExpiryDate"));
		}
		return serviceFeeExpiryDate;
	}
	
	//@SuppressWarnings("unchecked")
	public List<OfferInformation> getOfferInformationList() {
		if(offerInformationList == null) {
			offerInformationList = new ArrayList<OfferInformation>();
			Object[] offerInfoList = (Object[]) (((Map<?, ?>) result).get("offerInformationList"));
			if(offerInfoList!=null){
				for (Object object : offerInfoList) {
					offerInformationList.add(new OfferInformation((Map<String, Object>) object));
				}
			}
		}
		return offerInformationList;

	}

}

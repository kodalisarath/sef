package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AccBefAndAfterRef extends NativeAirResponse {

    private static final long serialVersionUID = 1L;

    public AccBefAndAfterRef(Map<String, Object> paramMap) {
	super(paramMap);
    }

    private Date serviceClassTemporaryExpiryDate = new Date();
    private Integer serviceClassOriginal;
    private Integer serviceClassCurrent;
    private List<AccountFlags> accountFlags;
    private String promotionPlanID;
    private Date serviceFeeExpiryDate = new Date();
    private Date supervisionExpiryDate = new Date();
    private Integer creditClearanceDate;
    private Date serviceRemovalDate = new Date();
    private String accountValue1;
    private String accountValue2;
    private List<DedicatedAccountInformation> dedicatedAccInfo;
    private List<UsageAccumulatorInfo> usageAccuInfo;
    private List<ServiceOffering> serviceOfferings;
    private List<Community> communityIdList;
    private List<OfferInformation> offerInformationList;

    public Date getServiceClassTemporaryExpiryDate() {
	if (serviceClassTemporaryExpiryDate == null) {
	    serviceClassTemporaryExpiryDate = getParam("serviceClassTemporaryExpiryDate", Date.class);
	}
	return serviceClassTemporaryExpiryDate;
    }

    public Integer getServiceClassOriginal() {
	if (serviceClassOriginal == null) {
	    serviceClassOriginal = getParam("serviceClassOriginal", Integer.class);
	}
	return serviceClassOriginal;
    }

    public Integer getServiceClassCurrent() {
	if (serviceClassCurrent == null) {
	    serviceClassCurrent = getParam("serviceClassCurrent", Integer.class);
	}
	return serviceClassCurrent;
    }

    public List<AccountFlags> getAccountFlags() {
	return accountFlags;
    }

    public String getPromotionPlanID() {
	if (promotionPlanID == null) {
	    promotionPlanID = getParam("promotionPlanID", String.class);
	}
	return promotionPlanID;
    }

    public Date getServiceFeeExpiryDate() {
	if (serviceFeeExpiryDate == null) {
	    serviceFeeExpiryDate = getParam("serviceFeeExpiryDate", Date.class);
	}
	return serviceFeeExpiryDate;
    }

    public Date getSupervisionExpiryDate() {
	if (supervisionExpiryDate == null) {
	    supervisionExpiryDate = getParam("supervisionExpiryDate", Date.class);
	}
	return supervisionExpiryDate;
    }

    public Integer getCreditClearanceDate() {
	if (creditClearanceDate == null) {
	    creditClearanceDate = getParam("creditClearanceDate", Integer.class);
	}
	return creditClearanceDate;
    }

    public Date getServiceRemovalDate() {
	if (serviceRemovalDate == null) {
	    serviceRemovalDate = getParam("serviceRemovalDate", Date.class);
	}
	return serviceRemovalDate;
    }

    public String getAccountValue1() {
	if (accountValue1 == null) {
	    accountValue1 = getParam("accountValue1", String.class);
	}
	return accountValue1;
    }

    public String getAccountValue2() {
	if (accountValue2 == null) {
	    accountValue2 = getParam("accountValue2", String.class);
	}
	return accountValue2;
    }

    public List<DedicatedAccountInformation> getDedicatedAccInfo() {

	if(dedicatedAccInfo == null || dedicatedAccInfo.isEmpty())
	{
	    Object[] dedicatedAccObjects = (Object[]) (paramMap.get("dedicatedAccountInformation"));


	    dedicatedAccInfo = new ArrayList<DedicatedAccountInformation>();
	    if(dedicatedAccObjects !=null)
	    {
		for (Object oi : dedicatedAccObjects) {
		    dedicatedAccInfo.add(new DedicatedAccountInformation((Map<String, Object>) oi));
		}
	    }
	}
	return dedicatedAccInfo;
    }

    public List<UsageAccumulatorInfo> getUsageAccuInfo() {

	return usageAccuInfo;
    }

    public List<ServiceOffering> getServiceOfferings() {

	if(serviceOfferings == null || serviceOfferings.isEmpty())
	{
	    Object[] serviceOfferingsObjects = (Object[]) (paramMap.get("serviceOfferings"));
	    serviceOfferings = new ArrayList<ServiceOffering>();

	    if(serviceOfferingsObjects!=null)
	    {
		for (Object oi : serviceOfferingsObjects) {
		    serviceOfferings.add(new ServiceOffering((Map<String, Object>) oi));
		}
	    }
	}
	return serviceOfferings;
    }

    public List<Community> getCommunityIdList() {
	return communityIdList;
    }

    public List<OfferInformation> getOfferInformationList() {

	if(offerInformationList== null|| offerInformationList.isEmpty())
	{
	    Object[] offerInformationObjects = (Object[]) (paramMap.get("offerInformationList"));


	    offerInformationList = new ArrayList<OfferInformation>();

	    if(offerInformationObjects!=null)
	    {
		for (Object oi : offerInformationObjects) {
		    offerInformationList.add(new OfferInformation((Map<String, Object>) oi));
		}
	    }
	}
	return offerInformationList;
    }

}

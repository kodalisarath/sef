package com.ericsson.raso.sef.client.air.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InstallSubscriberRequest extends AbstractAirRequest {
	
	public InstallSubscriberRequest() {
		super("InstallSubscriber");
	}

	private Boolean messageCapabilityFlag;
	private Integer promotionPlanId;
	private Integer serviceClassNew;
	private Integer languageIDNew;
	private Boolean temporaryBlockedFlag;
	private Integer ussdEndOfCallNotificationID;
	private Integer subscriberNumberNAI;

	private List<ServiceOffering> serviceOfferings;
	private List<PamInformation> pamInformationList;

	public Integer getServiceClassNew() {
		return serviceClassNew;
	}
	
	public Integer getLanguageIDNew() {
		return languageIDNew;
	}
	
	public void setLanguageIDNew(Integer languageIDNew) {
		this.languageIDNew = languageIDNew;
		addParam("languageIDNew", this.languageIDNew);
	}

	public void setServiceClassNew(Integer serviceClassNew) {
		this.serviceClassNew = serviceClassNew;
		addParam("serviceClassNew", this.serviceClassNew);
	}

	public List<ServiceOffering> getServiceOfferings() {
		return serviceOfferings;
	}

	public void setServiceOfferings(List<ServiceOffering> serviceOfferings) {
		this.serviceOfferings = serviceOfferings;
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (ServiceOffering serviceOffering : serviceOfferings) {
			list.add(serviceOffering.toNative());
		}
		addParam("serviceOfferings", list); 
		
	}

	public List<PamInformation> getPamInformationList() {
		return pamInformationList;
	}

	public void setPamInformationList(List<PamInformation> pamInformationList) {
		this.pamInformationList = pamInformationList;
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (PamInformation pamInformation : pamInformationList) {
			list.add(pamInformation.toNative());
		}
		addParam("pamInformationList", list);
	}

	public Boolean getMessageCapabilityFlag() {
		return messageCapabilityFlag;
	}

	public void setMessageCapabilityFlag(Boolean messageCapabilityFlag) {
		this.messageCapabilityFlag = messageCapabilityFlag;
	}

	public Integer getPromotionPlanId() {
		return promotionPlanId;
	}

	public void setPromotionPlanId(Integer promotionPlanId) {
		this.promotionPlanId = promotionPlanId;
		addParam("promotionPlanId", this.promotionPlanId);
	}

	public Boolean getTemporaryBlockedFlag() {
		return temporaryBlockedFlag;
	}

	public void setTemporaryBlockedFlag(Boolean temporaryBlockedFlag) {
		this.temporaryBlockedFlag = temporaryBlockedFlag;
		addParam("temporaryBlockedFlag", this.temporaryBlockedFlag);
	}

	public Integer getUssdEndOfCallNotificationID() {
		return ussdEndOfCallNotificationID;
	}

	public void setUssdEndOfCallNotificationID(Integer ussdEndOfCallNotificationID) {
		this.ussdEndOfCallNotificationID = ussdEndOfCallNotificationID;
		addParam("ussdEndOfCallNotificationID", this.ussdEndOfCallNotificationID);
	}

	public Integer getSubscriberNumberNAI() {
		return subscriberNumberNAI;
	}

	public void setSubscriberNumberNAI(Integer subscriberNumberNAI) {
		this.subscriberNumberNAI = subscriberNumberNAI;
		addParam("subscriberNumberNAI", this.subscriberNumberNAI);
	}
	
}

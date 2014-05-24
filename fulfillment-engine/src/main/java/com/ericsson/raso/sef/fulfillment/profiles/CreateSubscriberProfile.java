package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.entities.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class CreateSubscriberProfile extends BlockingFulfillment<Product> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean messageCapabilityFlag;
	private Integer promotionPlanId;
	private Integer serviceClassNew;
	private Integer languageIDNew;
	private Boolean temporaryBlockedFlag;
	private Integer ussdEndOfCallNotificationID;
	private String originOperatorId;
	
	private PamInformationList pamInformationList;
	
	protected CreateSubscriberProfile(String name) {
		super(name);
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
	}

	public Integer getServiceClassNew() {
		return serviceClassNew;
	}

	public void setServiceClassNew(Integer serviceClassNew) {
		this.serviceClassNew = serviceClassNew;
	}

	public Integer getLanguageIDNew() {
		return languageIDNew;
	}

	public void setLanguageIDNew(Integer languageIDNew) {
		this.languageIDNew = languageIDNew;
	}

	public Boolean getTemporaryBlockedFlag() {
		return temporaryBlockedFlag;
	}

	public void setTemporaryBlockedFlag(Boolean temporaryBlockedFlag) {
		this.temporaryBlockedFlag = temporaryBlockedFlag;
	}

	public Integer getUssdEndOfCallNotificationID() {
		return ussdEndOfCallNotificationID;
	}

	public void setUssdEndOfCallNotificationID(Integer ussdEndOfCallNotificationID) {
		this.ussdEndOfCallNotificationID = ussdEndOfCallNotificationID;
	}

	public String getOriginOperatorId() {
		return originOperatorId;
	}

	public void setOriginOperatorId(String originOperatorId) {
		this.originOperatorId = originOperatorId;
	}

	public PamInformationList getPamInformationList() {
		return pamInformationList;
	}

	public void setPamInformationList(PamInformationList pamInformationList) {
		this.pamInformationList = pamInformationList;
	}


	@Override
	public void fulfill(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}


	
}

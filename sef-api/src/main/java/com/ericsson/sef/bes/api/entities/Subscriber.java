package com.ericsson.sef.bes.api.entities;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subscriber")
public class Subscriber {

	private String				userId				= null;
	private String				customerId			= null;
	private String				contractId			= null;
	private String				msisdn				= null;
	private String				pin					= null;
	private String				email				= null;
	private String				imsi				= null;
	private String				imeiSv				= null;
	private String				paymentType			= null;
	private String				paymentResponsible	= null;
	private String				paymentParent		= null;
	private String				billCycleDay		= null;
	private String				contractState		= null;
	private Long				dateOfBirth			= null;
	private String				gender				= null;
	private String				prefferedLanguage	= null;
	private Long				registrationDate	= null;
	private Long				activeDate			= null;
	private String				ratePlan			= null;
	private String				customerSegment		= null;
	private Map<String, String>	metas				= null;

	
//	public com.ericsson.raso.sef.core.db.model.Subscriber persistableEntity() {
//		com.ericsson.raso.sef.core.db.model.Subscriber subscriber = new com.ericsson.raso.sef.core.db.model.Subscriber();
//		//TODO: perform the transformation
//		return subscriber;
//	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImeiSv() {
		return imeiSv;
	}

	public void setImeiSv(String imeiSv) {
		this.imeiSv = imeiSv;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentResponsible() {
		return paymentResponsible;
	}

	public void setPaymentResponsible(String paymentResponsible) {
		this.paymentResponsible = paymentResponsible;
	}

	public String getPaymentParent() {
		return paymentParent;
	}

	public void setPaymentParent(String paymentParent) {
		this.paymentParent = paymentParent;
	}

	public String getBillCycleDay() {
		return billCycleDay;
	}

	public void setBillCycleDay(String billCycleDay) {
		this.billCycleDay = billCycleDay;
	}

	public String getContractState() {
		return contractState;
	}

	public void setContractState(String contractState) {
		this.contractState = contractState;
	}

	public Long getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Long dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPrefferedLanguage() {
		return prefferedLanguage;
	}

	public void setPrefferedLanguage(String prefferedLanguage) {
		this.prefferedLanguage = prefferedLanguage;
	}

	public Long getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Long registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Long getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Long activeDate) {
		this.activeDate = activeDate;
	}

	public String getRatePlan() {
		return ratePlan;
	}

	public void setRatePlan(String ratePlan) {
		this.ratePlan = ratePlan;
	}

	public String getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	public Map<String, String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}
	
	public void addMeta(String key, String value) {
		if (this.metas == null)
			this.metas = new HashMap<String, String>();
		
		this.metas.put(key, value);
	}

}

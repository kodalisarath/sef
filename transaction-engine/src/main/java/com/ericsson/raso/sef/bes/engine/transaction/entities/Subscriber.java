package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.xpath.axes.SubContextList;
import org.joda.time.DateTime;

import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.core.db.model.SubscriberMeta;

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
	private Long				created				= null;
	private Long				lastModified		= null;
	private Map<String, String>	metas				= null;

	
	public Subscriber(com.ericsson.raso.sef.core.db.model.Subscriber other) {
		this.userId = other.getUserId();
		this.customerId = other.getCustomerId();
		this.contractId = other.getContractId();
		this.msisdn = other.getMsisdn();
		this.pin = other.getPin();
		this.email = other.getEmail();
		this.imsi = other.getImsi();
		this.imeiSv = other.getImeiSv();
		this.paymentType = other.getPaymentType();
		this.paymentResponsible = other.getPaymentResponsible();
		this.paymentParent = other.getPaymentParent();
		this.billCycleDay = other.getBillCycleDay();
		this.contractState = other.getContractState().getName(); //TODO: this must be aligned to the subscriber life-cycle model defined in the BC workflow product & SEF Framework states
		this.dateOfBirth = other.getDateOfBirth().getMillis();
		this.gender = other.getGender();
		this.prefferedLanguage = other.getPrefferedLanguage();
		this.registrationDate = other.getRegistrationDate().getMillis();
		this.activeDate = other.getActiveDate().getMillis();
		this.ratePlan = other.getRatePlan();
		this.customerSegment = other.getCustomerSegment();
		this.created = other.getCreated().getMillis();
		this.lastModified = other.getLastModified().getMillis();
		this.metas = this.genericMetas(other.getMetas());
		
		
	}
	
	//This is added as a parameterized constructor is already there
	public Subscriber(){
		
	}
	
	
	public com.ericsson.raso.sef.core.db.model.Subscriber persistableEntity() {
		com.ericsson.raso.sef.core.db.model.Subscriber subscriber = new com.ericsson.raso.sef.core.db.model.Subscriber();
//TODO: fix this code once the core.db package is refactored....
		subscriber.setUserId(this.userId);
		subscriber.setCustomerId(this.customerId);
		subscriber.setContractId(this.contractId);
		subscriber.setMsisdn(this.msisdn);
		subscriber.setPin(this.pin);
		subscriber.setEmail(this.email);
		subscriber.setImsi(this.imsi);
		subscriber.setImeiSv(this.imeiSv);
		subscriber.setPaymentType(this.paymentType);
		subscriber.setPaymentResponsible(this.paymentResponsible);
		subscriber.setPaymentParent(this.paymentParent);
		subscriber.setBillCycleDay(this.billCycleDay);
		subscriber.setContractState(ContractState.valueOf(this.contractState));
		subscriber.setDateOfBirth(new DateTime(this.dateOfBirth));
		subscriber.setGender(this.gender);
		subscriber.setPrefferedLanguage(this.prefferedLanguage);
		subscriber.setRegistrationDate(new DateTime(this.registrationDate));
		subscriber.setActiveDate(new DateTime(this.activeDate));
		subscriber.setRatePlan(this.ratePlan);
		subscriber.setCustomerSegment(this.customerSegment);
		subscriber.setCreated(new DateTime(this.created));
		subscriber.setLastModified(new DateTime(lastModified));
		subscriber.setMetas(this.nativeMetas(this.metas));
		
		return subscriber;
	}
	
	private Collection<SubscriberMeta> nativeMetas(Map<String, String> metas) {
		Collection<SubscriberMeta> nativeMetas = new ArrayList<SubscriberMeta>();
		for (String key: metas.keySet()) {
			SubscriberMeta meta = new SubscriberMeta();
			meta.setKey(key);
			meta.setValue(metas.get(key));
			nativeMetas.add(meta);
		}
		return nativeMetas;
	}


	private Map<String, String> genericMetas(Collection<SubscriberMeta> metas) {
		Map<String, String> subscriberMetas = new TreeMap<String, String>();
		
		for (SubscriberMeta meta: metas)
			subscriberMetas.put(meta.getKey(), meta.getValue());
		
		return subscriberMetas;
	}

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

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getLastModified() {
		return lastModified;
	}

	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}

	public Map<String, String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}

}

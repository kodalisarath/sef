package com.ericsson.raso.sef.core.db.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.core.Meta;

//TODO: Cleanup all SMART specific metas into SMART Front-End Project....
public class Subscriber implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 875848050925514465L;
	public static final String MSISDN = "MSISDN";
	public static final String CONTRACT_STATE = "contractState";
	
	
	private String userId;
	private String accountId;
	private String customerId;
	private String contractId;
	private String msisdn;
	private String pin;
	private String email;
	private String imsi;
	private String imeiSv;
	private String paymentType;
	private String paymentResponsible;
	private String paymentParent;
	private String billCycleDay;
	private ContractState contractState;
	private Date dateOfBirth;
	private String gender;
	private String prefferedLanguage;
	private Date registrationDate;
	private Date activeDate;
	private String ratePlan;
	private String customerSegment;
	

	private Collection<Meta> metas;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
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

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
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

	public Collection<Meta> getMetas() {
		if (metas == null) {
			metas = new ArrayList<Meta>();
		}
		return metas;
	}

	public void setMetas(Collection<Meta> metas) {
		this.metas = metas;
	}

	public ContractState getContractState() {
		return contractState;
	}

	public void setContractState(ContractState contractState) {
		this.contractState = contractState;
	}

	private Map<String, String> metaMap = new TreeMap<String, String>();

	public String getMetaValue(String key) {
		if (metaMap.size() == 0 && metas != null) {
			for (Meta meta : metas) {
				if (meta.getValue() != null && meta.getValue().trim().length() > 0) {
					metaMap.put(meta.getKey().toLowerCase(), meta.getValue());
				}
			}
		}
		return metaMap.get(key.toLowerCase());
	}
}

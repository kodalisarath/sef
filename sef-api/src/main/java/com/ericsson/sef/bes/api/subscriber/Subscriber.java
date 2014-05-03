package com.ericsson.sef.bes.api.subscriber;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="subscriberProfile")
public class Subscriber {

	private String userId;
	private String msisdn;
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
	
	private List<Meta> metas;

	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
	
	public ContractState getContractState() {
		return contractState;
	}
	
	public void setContractState(ContractState contractState) {
		this.contractState = contractState;
	}
	
	public List<Meta> getMetas() {
		return metas;
	}
	
	public void setMetas(List<Meta> metas) {
		this.metas = metas;
	}
}

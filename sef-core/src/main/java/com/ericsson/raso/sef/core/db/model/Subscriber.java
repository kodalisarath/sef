package com.ericsson.raso.sef.core.db.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateTime;

//TODO: Cleanup all SMART specific metas into SMART Front-End Project....
public class Subscriber {

	public static final List<String> metaKeys;

	public static final String MSISDN = "MSISDN";
	public static final String CONTRACT_STATE = "contractState";
	public static final String IsCFMOC = "IsCFMOC";
	public static final String IsCollectCallAllowed = "IsCollectCallAllowed";
	public static final String IsFirstCallPassed = "IsFirstCallPassed";
	public static final String IsGPRSUsed = "IsGPRSUsed";
	public static final String IsLastRechargeInfoStored = "IsLastRechargeInfoStored";
	public static final String IsLastTransactionEnqUsed = "IsLastTransactionEnqUsed";
	public static final String IsOperatorCollectCallAllowed = "IsOperatorCollectCallAllowed";
	public static final String IsSmsAllowed = "IsSmsAllowed";
	public static final String IsUSCAllowed = "IsUSCAllowed";
	public static final String PreActiveEndDate = "preActiveEndDate";
	public static final String PASALOAD_MONEY = "pasaload:money";
	public static final String packaze = "package";
	public static final String subscriberSite = "subscriber:site";
	public static final String vValidFrom = "vValidFrom";
	public static final String vInvalidFrom = "vInvalidFrom";
	public static final String Key = "Key";
	public static final String KeyType = "KeyType";
	public static final String s_CRMTitle = "s_CRMTitle";
	public static final String tagging = "tagging";
	public static final String IsBalanceClearanceOnOutpayment = "IsBalanceClearanceOnOutpayment";
	public static final String sOperatorCollectCallAllowed = "sOperatorCollectCallAllowed";
	public static final String S_OfferId = "S_OfferId";
	public static final String bValidFrom = "bValidFrom";
	public static final String bInvalidFrom = "bInvalidFrom";
	public static final String IsLocked = "IsLocked";

	static {
		metaKeys = new ArrayList<String>();
		metaKeys.add(IsCFMOC);
		metaKeys.add(IsCollectCallAllowed);
		metaKeys.add(IsCollectCallAllowed);
		metaKeys.add(IsFirstCallPassed);
		metaKeys.add(IsGPRSUsed);
		metaKeys.add(IsLastRechargeInfoStored);
		metaKeys.add(IsLastTransactionEnqUsed);
		metaKeys.add(IsOperatorCollectCallAllowed);
		metaKeys.add(IsUSCAllowed);
		metaKeys.add(IsSmsAllowed);
		metaKeys.add(PreActiveEndDate);
		metaKeys.add(PASALOAD_MONEY);
		metaKeys.add(packaze);
		metaKeys.add(subscriberSite);

		metaKeys.add(vValidFrom);
		metaKeys.add(vInvalidFrom);
		metaKeys.add(Key);
		metaKeys.add(KeyType);
		metaKeys.add(s_CRMTitle);
		metaKeys.add(tagging);
		metaKeys.add(IsBalanceClearanceOnOutpayment);
		metaKeys.add(sOperatorCollectCallAllowed);
		metaKeys.add(S_OfferId);
		metaKeys.add(bValidFrom);
		metaKeys.add(bInvalidFrom);
	}

	private String userId;
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
	private DateTime dateOfBirth;
	private String gender;
	private String prefferedLanguage;
	private DateTime registrationDate;
	private DateTime activeDate;
	private String ratePlan;
	private String customerSegment;
	private DateTime created;
	private DateTime lastModified;
	private Boolean deleted;

	private Collection<SubscriberMeta> metas;

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

	public DateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(DateTime dateOfBirth) {
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

	public DateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(DateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public DateTime getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(DateTime activeDate) {
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

	public Collection<SubscriberMeta> getMetas() {
		if (metas == null) {
			metas = new ArrayList<SubscriberMeta>();
		}
		return metas;
	}

	public void setMetas(Collection<SubscriberMeta> metas) {
		this.metas = metas;
	}

	public ContractState getContractState() {
		return contractState;
	}

	public void setContractState(ContractState contractState) {
		this.contractState = contractState;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public DateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(DateTime lastModified) {
		this.lastModified = lastModified;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	private Map<String, String> metaMap = new TreeMap<String, String>();

	public String getMetaValue(String key) {
		if (metaMap.size() == 0 && metas != null) {
			for (SubscriberMeta meta : metas) {
				if (meta.getValue() != null && meta.getValue().trim().length() > 0) {
					metaMap.put(meta.getKey().toLowerCase(), meta.getValue());
				}
			}
		}
		return metaMap.get(key.toLowerCase());
	}
}

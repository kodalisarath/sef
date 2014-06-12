package com.ericsson.raso.sef.smart.commons.read;

import java.util.ArrayList;
import java.util.List;

public class RopRead {

	@Override
	public String toString() {
		return "RopRead [customerId=" + customerId + ", key=" + key
				+ ", category=" + category + ", prefetchFilter="
				+ prefetchFilter + ", activeEndDate=" + activeEndDate
				+ ", annoFirstWarningPeriodSent=" + annoFirstWarningPeriodSent
				+ ", annoSecondWarningPeriodSent="
				+ annoSecondWarningPeriodSent + ", chargedMenuAccessCounter="
				+ chargedMenuAccessCounter + ", firstCallDate=" + firstCallDate
				+ ", graceEndDate=" + graceEndDate
				+ ", isBalanceClearanceOnOutpayment="
				+ isBalanceClearanceOnOutpayment + ", isCFMOC=" + isCFMOC
				+ ", IsCollectCallAllowed=" + IsCollectCallAllowed
				+ ", IsFirstCallPassed=" + IsFirstCallPassed + ", IsGPRSUsed="
				+ IsGPRSUsed + ", IsLastRechargeInfoStored="
				+ IsLastRechargeInfoStored + ", IsLastTransactionEnqUsed="
				+ IsLastTransactionEnqUsed + ", IsLocked=" + IsLocked
				+ ", IsOperatorCollectCallAllowed="
				+ IsOperatorCollectCallAllowed + ", IsSmsAllowed="
				+ IsSmsAllowed + ", IsUSCAllowed=" + IsUSCAllowed
				+ ", preActiveEndDate=" + preActiveEndDate
				+ ", cTaggingStatus=" + cTaggingStatus + ", s_CRMTitle="
				+ s_CRMTitle + ", lastKnownPeriod=" + lastKnownPeriod + "]";
	}

	private String customerId;
	private Integer key;
	private String category;
	private Integer prefetchFilter;
	private String activeEndDate;
	private Boolean annoFirstWarningPeriodSent;
	private Boolean annoSecondWarningPeriodSent;
	private List<Integer> chargedMenuAccessCounter = new ArrayList<Integer>();
	private String firstCallDate;
	private String graceEndDate;
	private Boolean isBalanceClearanceOnOutpayment;
	private Integer isCFMOC;
	private Boolean IsCollectCallAllowed;
	private Boolean IsFirstCallPassed;
	private Boolean IsGPRSUsed;
	private Boolean IsLastRechargeInfoStored;
	private Boolean IsLastTransactionEnqUsed;
	private Boolean IsLocked;
	private Boolean IsOperatorCollectCallAllowed;
	private Boolean IsSmsAllowed;
	private Boolean IsUSCAllowed;
	private String preActiveEndDate;
	private Integer cTaggingStatus;
	private String s_CRMTitle;
	private String lastKnownPeriod;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getPrefetchFilter() {
		return prefetchFilter;
	}

	public void setPrefetchFilter(Integer prefetchFilter) {
		this.prefetchFilter = prefetchFilter;
	}

	public String getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(String activeEndDate) {
		this.activeEndDate = activeEndDate;
	}

	public Boolean getAnnoFirstWarningPeriodSent() {
		return annoFirstWarningPeriodSent;
	}

	public void setAnnoFirstWarningPeriodSent(Boolean annoFirstWarningPeriodSent) {
		this.annoFirstWarningPeriodSent = annoFirstWarningPeriodSent;
	}

	public Boolean getAnnoSecondWarningPeriodSent() {
		return annoSecondWarningPeriodSent;
	}

	public void setAnnoSecondWarningPeriodSent(Boolean annoSecondWarningPeriodSent) {
		this.annoSecondWarningPeriodSent = annoSecondWarningPeriodSent;
	}

	public List<Integer> getChargedMenuAccessCounter() {
		return chargedMenuAccessCounter;
	}

	public void setChargedMenuAccessCounter(List<Integer> chargedMenuAccessCounter) {
		this.chargedMenuAccessCounter = chargedMenuAccessCounter;
	}

	public String getFirstCallDate() {
		return firstCallDate;
	}

	public void setFirstCallDate(String firstCallDate) {
		this.firstCallDate = firstCallDate;
	}

	public String getGraceEndDate() {
		return graceEndDate;
	}

	public void setGraceEndDate(String graceEndDate) {
		this.graceEndDate = graceEndDate;
	}

	public Boolean getIsBalanceClearanceOnOutpayment() {
		return isBalanceClearanceOnOutpayment;
	}

	public void setIsBalanceClearanceOnOutpayment(Boolean isBalanceClearanceOnOutpayment) {
		this.isBalanceClearanceOnOutpayment = isBalanceClearanceOnOutpayment;
	}

	public Integer getIsCFMOC() {
		return isCFMOC;
	}

	public void setIsCFMOC(Integer isCFMOC) {
		this.isCFMOC = isCFMOC;
	}

	public Boolean getIsCollectCallAllowed() {
		return IsCollectCallAllowed;
	}

	public void setIsCollectCallAllowed(Boolean isCollectCallAllowed) {
		IsCollectCallAllowed = isCollectCallAllowed;
	}

	public Boolean getIsFirstCallPassed() {
		return IsFirstCallPassed;
	}

	public void setIsFirstCallPassed(Boolean isFirstCallPassed) {
		IsFirstCallPassed = isFirstCallPassed;
	}

	public Boolean getIsGPRSUsed() {
		return IsGPRSUsed;
	}

	public void setIsGPRSUsed(Boolean isGPRSUsed) {
		IsGPRSUsed = isGPRSUsed;
	}

	public Boolean getIsLastRechargeInfoStored() {
		return IsLastRechargeInfoStored;
	}

	public void setIsLastRechargeInfoStored(Boolean isLastRechargeInfoStored) {
		IsLastRechargeInfoStored = isLastRechargeInfoStored;
	}

	public Boolean getIsLastTransactionEnqUsed() {
		return IsLastTransactionEnqUsed;
	}

	public void setIsLastTransactionEnqUsed(Boolean isLastTransactionEnqUsed) {
		IsLastTransactionEnqUsed = isLastTransactionEnqUsed;
	}

	public Boolean getIsLocked() {
		return IsLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		IsLocked = isLocked;
	}

	public Boolean getIsOperatorCollectCallAllowed() {
		return IsOperatorCollectCallAllowed;
	}

	public void setIsOperatorCollectCallAllowed(Boolean isOperatorCollectCallAllowed) {
		IsOperatorCollectCallAllowed = isOperatorCollectCallAllowed;
	}

	public Boolean getIsSmsAllowed() {
		return IsSmsAllowed;
	}

	public void setIsSmsAllowed(Boolean isSmsAllowed) {
		IsSmsAllowed = isSmsAllowed;
	}

	public Boolean getIsUSCAllowed() {
		return IsUSCAllowed;
	}

	public void setIsUSCAllowed(Boolean isUSCAllowed) {
		IsUSCAllowed = isUSCAllowed;
	}

	public String getPreActiveEndDate() {
		return preActiveEndDate;
	}

	public void setPreActiveEndDate(String preActiveEndDate) {
		this.preActiveEndDate = preActiveEndDate;
	}

	public Integer getcTaggingStatus() {
		return cTaggingStatus;
	}

	public void setcTaggingStatus(Integer cTaggingStatus) {
		this.cTaggingStatus = cTaggingStatus;
	}

	public String getS_CRMTitle() {
		return s_CRMTitle;
	}

	public void setS_CRMTitle(String s_CRMTitle) {
		this.s_CRMTitle = s_CRMTitle;
	}

	public String getLastKnownPeriod() {
		return lastKnownPeriod;
	}

	public void setLastKnownPeriod(String lastKnownPeriod) {
		this.lastKnownPeriod = lastKnownPeriod;
	}
}

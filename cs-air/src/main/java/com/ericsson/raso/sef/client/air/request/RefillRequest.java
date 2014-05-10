package com.ericsson.raso.sef.client.air.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefillRequest extends AbstractAirRequest {

	public RefillRequest() {
		super("Refill");

	}

	private String refProfID;
	private Integer refType;
	private String transacAmount;
	private String transacCurrency;
	private String transacCode;
	private String transactionType;
	private MessageCapabiity messageCapabilityFlag;
	private boolean RefAccBeforeFlag;
	private boolean RefAccAfterFlag;
	private boolean requestRefillDetailsFlag;
	private boolean SubDedicatedFlag;
	private String externalData1;
	private String externalData2;
	private String externalData3;
	private String externalData4;
	private String voucherActivationCode;
	private Integer selectedOption;
	private Integer locationNumber;
	private Integer locationNumberNAI;
	private String cellIdentifier;
	private boolean validateSubscriberLocation;
	private List<TreeField> treeDefinedField;
	private Integer[] negotiatedCapabilities;

	public String getExternalData1() {
		return externalData1;
	}

	public void setExternalData1(String externalData1) {
		this.externalData1 = externalData1;
		addParam("externalData1", this.externalData1);
	}

	public String getExternalData2() {
		return externalData2;
	}

	public void setExternalData2(String externalData2) {
		this.externalData2 = externalData2;
		addParam("externalData2", this.externalData2);
	}

	public String getExternalData3() {
		return externalData3;
	}

	public void setExternalData3(String externalData3) {
		this.externalData3 = externalData3;
		addParam("externalData3", this.externalData3);
	}

	public String getExternalData4() {
		return externalData4;
	}

	public void setExternalData4(String externalData4) {
		this.externalData4 = externalData4;
		addParam("externalData4", this.externalData4);
	}

	public String getRefProfID() {
		return refProfID;
	}

	public void setRefProfID(String refProfID) {
		this.refProfID = refProfID;
		addParam("refillProfileID", this.refProfID);
	}

	public Integer getRefType() {
		return refType;
	}

	public void setRefType(Integer refType) {
		this.refType = refType;
		addParam("refillType", this.refType);
	}

	public String getTransacAmount() {
		return transacAmount;
	}

	public void setTransacAmount(String transacAmount) {
		this.transacAmount = transacAmount;
		addParam("transactionAmount", this.transacAmount);
	}

	public String getTransacCurrency() {
		return transacCurrency;
	}

	public void setTransacCurrency(String transacCurrency) {
		this.transacCurrency = transacCurrency;
		addParam("transactionCurrency", this.transacCurrency);
	}

	public String getTransacCode() {
		return transacCode;
	}

	public void setTransacCode(String transacCode) {
		this.transacCode = transacCode;
		addParam("transactionCode", this.transacCode);
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
		addParam("transactionType", this.transactionType);
	}

	public MessageCapabiity getMessageCapabilityFlag() {
		return messageCapabilityFlag;
	}

	public void setMessageCapabilityFlag(MessageCapabiity messageCapabilityFlag) {
		this.messageCapabilityFlag = messageCapabilityFlag;
		addParam("messageCapabilityFlag", this.messageCapabilityFlag);
	}

	public boolean isRefAccBeforeFlag() {
		return RefAccBeforeFlag;
	}

	public void setRefAccBeforeFlag(boolean RefAccBeforeFlag) {
		this.RefAccBeforeFlag = RefAccBeforeFlag;
		addParam("requestRefillAccountBeforeFlag", this.RefAccBeforeFlag);
	}

	public boolean isRefAccAfterFlag() {
		return RefAccAfterFlag;
	}

	public void setRefAccAfterFlag(boolean RefAccAfterFlag) {
		this.RefAccAfterFlag = RefAccAfterFlag;
		addParam("requestRefillAccountAfterFlag", this.RefAccAfterFlag);
	}

	public boolean isRequestRefillDetailsFlag() {
		return requestRefillDetailsFlag;
	}

	public void setRequestRefillDetailsFlag(boolean requestRefillDetailsFlag) {
		this.requestRefillDetailsFlag = requestRefillDetailsFlag;
		addParam("requestRefillDetailsFlag", this.requestRefillDetailsFlag);
	}

	public boolean isSubDedicatedFlag() {
		return SubDedicatedFlag;
	}

	public void setSubDedicatedFlag(boolean subDedicatedFlag) {
		SubDedicatedFlag = subDedicatedFlag;
		addParam("SubDedicatedFlag", this.SubDedicatedFlag);
	}

	public String getVoucherActivationCode() {
		return voucherActivationCode;
	}

	public void setVoucherActivationCode(String voucherActivationCode) {
		this.voucherActivationCode = voucherActivationCode;
		addParam("voucherActivationCode", this.voucherActivationCode);
	}

	public Integer getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(Integer selectedOption) {
		this.selectedOption = selectedOption;
		addParam("selectedOption", this.selectedOption);
	}

	public Integer getLocationNumber() {
		return locationNumber;
	}

	public void setLocationNumber(Integer locationNumber) {
		this.locationNumber = locationNumber;
		addParam("locationNumber", this.locationNumber);
	}

	public Integer getLocationNumberNAI() {
		return locationNumberNAI;
	}

	public void setLocationNumberNAI(Integer locationNumberNAI) {
		this.locationNumberNAI = locationNumberNAI;
		addParam("locationNumberNAI", this.locationNumberNAI);
	}

	public String getCellIdentifier() {
		return cellIdentifier;
	}

	public void setCellIdentifier(String cellIdentifier) {
		this.cellIdentifier = cellIdentifier;
		addParam("cellIdentifier", this.cellIdentifier);
	}

	public boolean isValidateSubscriberLocation() {
		return validateSubscriberLocation;
	}

	public void setValidateSubscriberLocation(boolean validateSubscriberLocation) {
		this.validateSubscriberLocation = validateSubscriberLocation;
		addParam("validateSubscriberLocation", this.validateSubscriberLocation);
	}

	public List<TreeField> getTreeDefinedField() {
		return treeDefinedField;
	}

	public void setTreeDefinedField(List<TreeField> treeDefinedField) {
		this.treeDefinedField = treeDefinedField;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (TreeField tree : treeDefinedField) {
			list.add(tree.toNative());
		}
		addParam("treeDefinedField", list);
	}

	public Integer[] getNegotiatedCapabilities() {
		return negotiatedCapabilities;
	}

	public void setNegotiatedCapabilities(Integer[] negotiatedCapabilities) {
		this.negotiatedCapabilities = negotiatedCapabilities;
		addParam("negotiatedCapabilities", this.negotiatedCapabilities);

	}

}

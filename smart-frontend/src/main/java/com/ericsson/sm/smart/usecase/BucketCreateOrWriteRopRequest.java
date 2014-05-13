package com.ericsson.sm.smart.usecase;

import java.util.List;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.SymbolicParameter;

public class BucketCreateOrWriteRopRequest extends SmartRequest {

	private String customerId;
	private int key;
	private String category;
	private String bValidFrom;
	private String bInvalidFrom;
	private String OnPeakAccountID_FU;
	private long messageId;
	private String s_OfferId;

	public String getS_OfferId() {
		return s_OfferId;
	}

	public void setS_OfferId(String s_OfferId) {
		this.s_OfferId = s_OfferId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getbValidFrom() {
		return bValidFrom;
	}

	public void setbValidFrom(String bValidFrom) {
		this.bValidFrom = bValidFrom;
	}

	public String getbInvalidFrom() {
		return bInvalidFrom;
	}

	public void setbInvalidFrom(String bInvalidFrom) {
		this.bInvalidFrom = bInvalidFrom;
	}

	public String getOnPeakAccountID_FU() {
		return OnPeakAccountID_FU;
	}

	public void setOnPeakAccountID_FU(String onPeakAccountID_FU) {
		OnPeakAccountID_FU = onPeakAccountID_FU;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	@Override
	public void prepareRequest(Operation operation) {
		List<Object> parameters = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		for (Object param : parameters) {
			if (param instanceof StringParameter) {
				StringParameter parameter = (StringParameter) param;
				this.setCustomerId(parameter.getValue().trim());
			} else if (param instanceof EnumerationValueParameter) {
				EnumerationValueParameter parameter = (EnumerationValueParameter) param;
				this.setCategory(parameter.getValue().trim());
			} else if (param instanceof SymbolicParameter) {
				SymbolicParameter parameter = (SymbolicParameter) param;
				if (parameter.getName().equalsIgnoreCase("bValidFrom")) {
					this.setbValidFrom(parameter.getValue().trim());
				} else if (parameter.getName().equalsIgnoreCase("bInvalidFrom")) {
					this.setbInvalidFrom(parameter.getValue().trim());
				}
			} else if (param instanceof IntParameter) {
				IntParameter parameter = (IntParameter) param;
				this.setKey(parameter.getValue());
			} else if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			}
		}
	}

}

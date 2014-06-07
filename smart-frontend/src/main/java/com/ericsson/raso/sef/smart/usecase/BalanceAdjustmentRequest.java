package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;

public class BalanceAdjustmentRequest extends SmartRequest {

	private String customerId;
	private String accessKey;
	private String balanceId;
	private Long amountOfUnits;
	private Long chargeCode;
	private Long messageId;
	private String eventInfo;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(String balanceId) {
		this.balanceId = balanceId;
	}

	public Long getAmountOfUnits() {
		return amountOfUnits;
	}

	public void setAmountOfUnits(Long amountOfUnits) {
		this.amountOfUnits = amountOfUnits;
	}

	public Long getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(Long chargeCode) {
		this.chargeCode = chargeCode;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(String eventInfo) {
		this.eventInfo = eventInfo;
	}

	@Override
	public void prepareRequest(Operation operation) {
		List<Object> parameters = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();
		for (Object param : parameters) {
			if (param instanceof StringParameter) {
				StringParameter parameter = (StringParameter) param;
				if(parameter.getName().equalsIgnoreCase(CUSTOMER_ID)) {
					this.setCustomerId(parameter.getValue().trim());
				} else if(parameter.getName().equalsIgnoreCase(ACCESS_KEY)) {
					this.setAccessKey(parameter.getValue().trim());
				} else if(parameter.getName().equalsIgnoreCase("BalanceId")) {
					this.setBalanceId(parameter.getValue().trim());
				} else if(parameter.getName().equalsIgnoreCase("EventInfo")) {
					this.setEventInfo(parameter.getValue().trim());
				}
			} else if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				if(parameter.getName().equalsIgnoreCase("AmountOfUnits")) {
					this.setAmountOfUnits(parameter.getValue());
				} else if(parameter.getName().equalsIgnoreCase("ChargeCode")) {
					this.setChargeCode(parameter.getValue());
				} else if(parameter.getName().equalsIgnoreCase("MessageId")) {
					this.setMessageId(parameter.getValue());
				} 
			}
		}
	}

	@Override
	public String toString() {
		return "BalanceAdjustmentRequest [customerId=" + customerId
				+ ", accessKey=" + accessKey + ", balanceId=" + balanceId
				+ ", amountOfUnits=" + amountOfUnits + ", chargeCode="
				+ chargeCode + ", messageId=" + messageId + ", eventInfo="
				+ eventInfo + "]";
	}

}

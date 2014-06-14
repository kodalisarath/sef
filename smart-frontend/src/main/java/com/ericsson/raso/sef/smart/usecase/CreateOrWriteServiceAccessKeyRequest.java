package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.SymbolicParameter;

public class CreateOrWriteServiceAccessKeyRequest extends SmartRequest {

	private String customerId;
	private String category;
	private String keyType;
	private String vValidFrom;
	private long messageId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getvValidFrom() {
		return vValidFrom;
	}

	public void setvValidFrom(String vValidFrom) {
		this.vValidFrom = vValidFrom;
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
			if(param instanceof StringParameter) {
				StringParameter parameter = (StringParameter) param;
				if(parameter.getName().equalsIgnoreCase(OWING_CUSTOMER_ID)) {
					this.setCustomerId(parameter.getValue().trim());
				} else if (parameter.getName().equalsIgnoreCase(KEY)) {
					this.setCategory(parameter.getValue().trim());
				}
			} else if (param instanceof EnumerationValueParameter) {
				EnumerationValueParameter parameter = (EnumerationValueParameter) param;
				this.setKeyType(parameter.getValue().trim());
			} else if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			}
			 else if (param instanceof SymbolicParameter) {
				 SymbolicParameter parameter = (SymbolicParameter) param;
				 this.setvValidFrom(parameter.getValue());
				}
		}
	}

	@Override
	public String toString() {
		return "CreateOrWriteServiceAccessKeyRequest [customerId=" + customerId
				+ ", category=" + category + ", keyType=" + keyType
				+ ", vInvalidFrom="  + ", messageId=" + messageId
				+ "]";
	}

}

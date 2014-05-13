package com.ericsson.sm.smart.usecase;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;

public class CreateOrWriteServiceAccessKeyRequest extends SmartRequest {

	private String customerId;
	private String category;
	private String keyType;
	private XMLGregorianCalendar vInvalidFrom;
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

	public XMLGregorianCalendar getvInvalidFrom() {
		return vInvalidFrom;
	}

	public void setvInvalidFrom(XMLGregorianCalendar vInvalidFrom) {
		this.vInvalidFrom = vInvalidFrom;
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
			} else if (param instanceof DateParameter) {
				DateParameter parameter = (DateParameter) param;
				this.setvInvalidFrom(parameter.getValue());
			} else if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			}
		}
	}

}

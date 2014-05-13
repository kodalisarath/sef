package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;

public class VersionCreateOrWriteCustomerRequest extends SmartRequest {

	private String customerId;
	private String category;
	private XMLGregorianCalendar vValidFrom;
	private XMLGregorianCalendar vInvalidFrom;
	private Long messageId;

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

	public XMLGregorianCalendar getvValidFrom() {
		return vValidFrom;
	}

	public void setvValidFrom(XMLGregorianCalendar vValidFrom) {
		this.vValidFrom = vValidFrom;
	}

	public XMLGregorianCalendar getvInvalidFrom() {
		return vInvalidFrom;
	}

	public void setvInvalidFrom(XMLGregorianCalendar vInvalidFrom) {
		this.vInvalidFrom = vInvalidFrom;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
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
			} else if (param instanceof DateParameter) {
				DateParameter parameter = (DateParameter) param;
				if(parameter.getName().equalsIgnoreCase("vValidFrom")) {
					this.setvValidFrom(parameter.getValue());
				} else if (parameter.getName().equalsIgnoreCase("vInvalidFrom")) {
					this.setvInvalidFrom(parameter.getValue());
				}
			} else if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			}
		}
	}

}

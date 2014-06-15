package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StructParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.SymbolicParameter;

public class VersionCreateOrWriteROPRequest extends SmartRequest {

	private String customerId;
	private int key;
	private String vValidFrom;
	private String vInvalidFrom;
	private String s_OfferId;
	private String category;
	private long messageId;
	private int precision;
	private XMLGregorianCalendar expiryDate;

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

	public String getvValidFrom() {
		return vValidFrom;
	}

	public void setvValidFrom(String vValidFrom) {
		this.vValidFrom = vValidFrom;
	}

	public String getvInvalidFrom() {
		return vInvalidFrom;
	}

	public void setvInvalidFrom(String vInvalidFrom) {
		this.vInvalidFrom = vInvalidFrom;
	}

	public String getS_OfferId() {
		return s_OfferId;
	}

	public void setS_OfferId(String s_OfferId) {
		this.s_OfferId = s_OfferId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public XMLGregorianCalendar getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(XMLGregorianCalendar expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public void prepareRequest(Operation operation) {
		List<Object> parameters = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();
		for (Object param : parameters) {
			if (param instanceof StringParameter) {
				StringParameter parameter = (StringParameter) param;
				if (parameter.getName().equalsIgnoreCase(CUSTOMER_ID)) {
					this.setCustomerId(parameter.getValue().trim());
				}  else if (parameter.getName().equalsIgnoreCase("s_OfferId")) {
					this.setS_OfferId(parameter.getValue().trim());
				}

			} else if (param instanceof IntParameter) {
				IntParameter parameter = (IntParameter) param;
				this.setKey(parameter.getValue());
			}else if (param instanceof SymbolicParameter) {
				SymbolicParameter parameter = (SymbolicParameter) param;
				if(parameter.getName().equalsIgnoreCase("vValidFrom")){
					this.setvValidFrom(parameter.getValue());
				}else if(parameter.getName().equalsIgnoreCase("vInvalidFrom")){
					this.setvInvalidFrom(parameter.getValue());
				}
				
			}
			else if (param instanceof EnumerationValueParameter) {
				EnumerationValueParameter parameter = (EnumerationValueParameter) param;
				this.setCategory(parameter.getValue().trim());
			} else if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			} else if (param instanceof StructParameter) {
				StructParameter structParameter = (StructParameter) param;
				List<Object> struct = structParameter.getParameterOrBooleanParameterOrByteParameter();
				for (Object params : struct) {
					if (params instanceof IntParameter) {
						IntParameter parameter = (IntParameter) params;
						this.setPrecision(parameter.getValue());
					} else if (params instanceof DateTimeParameter) {
						DateTimeParameter parameter = (DateTimeParameter) params;
						this.setExpiryDate(parameter.getValue());
					}
				}

			}
		}
	}

	@Override
	public String toString() {
		return "VersionCreateOrWriteROPRequest [customerId=" + customerId
				+ ", key=" + key + ", vValidFrom=" + vValidFrom
				+ ", vInvalidFrom=" + vInvalidFrom + ", s_OfferId=" + s_OfferId
				+ ", category=" + category + ", messageId=" + messageId
				+ ", precision=" + precision + ", expiryDate=" + expiryDate
				+ "]";
	}

}

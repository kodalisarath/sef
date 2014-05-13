package com.ericsson.sm.smart.usecase;

import java.util.List;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ShortParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;

public class ModifyCustomerPreActiveRequest extends SmartRequest {

	private String customerId;
	private int daysOfExtension;
	private int eventInfo;
	private long messageId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public int getDaysOfExtension() {
		return daysOfExtension;
	}

	public void setDaysOfExtension(int daysOfExtension) {
		this.daysOfExtension = daysOfExtension;
	}

	public int getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(int eventInfo) {
		this.eventInfo = eventInfo;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	@Override
	public void prepareRequest(Operation operation) {
		List<Object> parameters = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();
		for (Object param : parameters) {
			if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			} else if (param instanceof ShortParameter) {
				ShortParameter parameter = (ShortParameter) param;
				this.setEventInfo(parameter.getValue());
			} else if (param instanceof IntParameter) {
				IntParameter parameter = (IntParameter) param;
				this.setDaysOfExtension(parameter.getValue());
			} else if (param instanceof StringParameter) {
				StringParameter parameter = (StringParameter) param;
				this.setCustomerId(parameter.getValue().trim());
			}
		}
	}

}

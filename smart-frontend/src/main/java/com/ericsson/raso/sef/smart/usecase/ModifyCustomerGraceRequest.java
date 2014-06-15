package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;

public class ModifyCustomerGraceRequest extends SmartRequest {

	private String customerId;
	private int daysOfExtension;
	private String eventInfo;
	private long messageId;
	private String accessKey;
	

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

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

	public String getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(String eventInfo) {
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
		List<Object> parameters = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		for (Object param : parameters) {
			if(param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				if(parameter.getName().equalsIgnoreCase("MessageId")){
					this.setMessageId(parameter.getValue());
				}
			}  else if (param instanceof IntParameter) {
				IntParameter parameter = (IntParameter) param;
				if(parameter.getName().equalsIgnoreCase("DaysOfExtension")){
					this.setDaysOfExtension(parameter.getValue());
				}
				
			} else if (param instanceof StringParameter) {
				StringParameter parameter = (StringParameter) param;
				if(parameter.getName().equalsIgnoreCase("EventInfo")){
					this.setEventInfo(parameter.getValue().trim());
				}else if(parameter.getName().equalsIgnoreCase("CustomerId")){
					this.setCustomerId(parameter.getValue().trim());
				}
				else if(parameter.getName().equalsIgnoreCase("AccessKey")){
					this.setAccessKey(parameter.getValue().trim());
				}
				
			}
		}
		
	}

	@Override
	public String toString() {
		return "ModifyCustomerGraceRequest [customerId=" + customerId
				+ ", daysOfExtension=" + daysOfExtension + ", eventInfo="
				+ eventInfo + ", messageId=" + messageId + "]";
	}

}

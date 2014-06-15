package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ShortParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;

public class ModifyTaggingRequest extends SmartRequest {

	private String customerId;
	private int tagging;
	private String eventInfo;
	private long messageId;
	private String accessKey;

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
	
	public int getTagging() {
		return tagging;
	}

	public void setTagging(int tagging) {
		this.tagging = tagging;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
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
		List<Object> parameters = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		for (Object param : parameters) {
			if(param instanceof StringParameter) {
				StringParameter parameter = (StringParameter) param;
				if (parameter.getName().equals(new String("CustomerId"))) {
					this.setCustomerId(parameter.getValue().trim());
				} else if (parameter.getName().equals(new String("AccessKey"))) {
					this.setAccessKey(parameter.getValue().trim());
				} else if (parameter.getName().equals(new String("EventInfo"))) {
					this.setEventInfo(parameter.getValue().trim());
				}
			} else if (param instanceof IntParameter) {
				IntParameter parameter = (IntParameter) param;
				if(parameter.getName().equalsIgnoreCase("Tagging")){
					this.setTagging(parameter.getValue());
				}
			}
			 else if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			}
		}
	}

	@Override
	public String toString() {
		return "ModifyTaggingRequest [customerId=" + customerId + ", tagging="
				+ tagging + ", eventInfo=" + eventInfo + ", messageId="
				+ messageId + ", accessKey=" + accessKey + "]";
	}
}

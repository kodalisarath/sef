package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;

public class SubscribePackageItemRequest extends SmartRequest {

	private String customerId;
	private String accessKey;
	private String packaze;
	private long messageId;

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

	public String getPackaze() {
		return packaze;
	}

	public void setPackaze(String packaze) {
		this.packaze = packaze;
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
				if(parameter.getName().equalsIgnoreCase("CustomerId")) {
					this.setCustomerId(parameter.getValue().trim());
				} else if(parameter.getName().equalsIgnoreCase("AccessKey")) {
					this.setAccessKey(parameter.getValue().trim());
				} else if(parameter.getName().equalsIgnoreCase("Package")) {
					this.setPackaze(parameter.getValue().trim());
				}
				
			} else if(param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			}
		}
	}

}

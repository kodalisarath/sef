package com.ericsson.raso.sef.smppgateway;

import java.io.Serializable;

public class SmppMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private String destinationMsisdn;
	private String messageBody;

	public String getDestinationMsisdn() {
		return destinationMsisdn;
	}

	public void setDestinationMsisdn(String destinationMsisdn) {
		this.destinationMsisdn = destinationMsisdn;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
}

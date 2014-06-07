package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;

public class RechargeRequest extends SmartRequest {
	private static final Logger logger = LoggerFactory.getLogger(RechargeRequest.class);
	private static final String EVENT_NAME = "EventName";
	private static final String EVENT_INFO = "EventInfo";
	private static final String EVENT_CLASS = "EventClass";
	private static final String MESSAGE_ID = "MessageId";
	private static final String RATING_INPUT = "RatingInput";
	private static final String AMOUNT_OF_UNITS = "AmountOfUnits";

	private String customerId;
	private String eventName;
	private Long amountOfUnits;
	private String eventClass;
	private String ratingInput0;
	private String ratingInput1;
	private String ratingInput2;
	private String ratingInput3;
	private String ratingInput4;
	private long messageId;
	private String eventInfo;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Long getAmountOfUnits() {
		return amountOfUnits;
	}

	public void setAmountOfUnits(Long amountOfUnits) {
		this.amountOfUnits = amountOfUnits;
	}

	public String getEventClass() {
		return eventClass;
	}

	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
	}

	public String getRatingInput0() {
		return ratingInput0;
	}

	public void setRatingInput0(String ratingInput0) {
		this.ratingInput0 = ratingInput0;
	}

	public String getRatingInput1() {
		return ratingInput1;
	}

	public void setRatingInput1(String ratingInput1) {
		this.ratingInput1 = ratingInput1;
	}

	public String getRatingInput2() {
		return ratingInput2;
	}

	public void setRatingInput2(String ratingInput2) {
		this.ratingInput2 = ratingInput2;
	}

	public String getRatingInput3() {
		return ratingInput3;
	}

	public void setRatingInput3(String ratingInput3) {
		this.ratingInput3 = ratingInput3;
	}

	public String getRatingInput4() {
		return ratingInput4;
	}

	public void setRatingInput4(String ratingInput4) {
		this.ratingInput4 = ratingInput4;
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
		logger.debug("parameters recieved of size"+parameters.size());
		for (Object param : parameters) {
			if(param instanceof StringParameter) {
				logger.debug("param is of StringParameter");
				StringParameter parameter = (StringParameter) param;
				if(parameter.getName().equalsIgnoreCase(CUSTOMER_ID)) {
					this.setCustomerId(parameter.getValue().trim());
				} else if(parameter.getName().equalsIgnoreCase(ACCESS_KEY)) {
					this.setCustomerId(parameter.getValue().trim());
				} else if(parameter.getName().equalsIgnoreCase(EVENT_NAME)) {
					this.setEventName(parameter.getValue().trim().replaceFirst("Rev", ""));
				} else if(parameter.getName().equalsIgnoreCase(EVENT_CLASS)) {
					this.setEventClass(parameter.getValue().trim());
				} else if(parameter.getName().equalsIgnoreCase(EVENT_INFO)) {
					this.setEventInfo(parameter.getValue().trim());
				}  
			} else if(param instanceof ListParameter) {
				logger.debug("param is of ListParameter");
				ListParameter listParameter = (ListParameter) param;
				if(listParameter.getName().equalsIgnoreCase(RATING_INPUT)) {
					List<Object> list = listParameter.getElementOrBooleanElementOrByteElement();
					switch (list.size()) {
					case 1:
						ratingInput0 = ((StringElement) list.get(0)).getValue();
						break;
					case 2:
						ratingInput0 = ((StringElement) list.get(0)).getValue();
						ratingInput1 = ((StringElement) list.get(1)).getValue();
						break;
					case 3:
						ratingInput0 = ((StringElement) list.get(0)).getValue();
						ratingInput1 = ((StringElement) list.get(1)).getValue();
						ratingInput2 = ((StringElement) list.get(2)).getValue();
						break;
					case 4:
						ratingInput0 = ((StringElement) list.get(0)).getValue();
						ratingInput1 = ((StringElement) list.get(1)).getValue();
						ratingInput2 = ((StringElement) list.get(2)).getValue();
						ratingInput3 = ((StringElement) list.get(3)).getValue();
						break;
					case 5:
						ratingInput0 = ((StringElement) list.get(0)).getValue();
						ratingInput1 = ((StringElement) list.get(1)).getValue();
						ratingInput2 = ((StringElement) list.get(2)).getValue();
						ratingInput3 = ((StringElement) list.get(3)).getValue();
						ratingInput4 = ((StringElement) list.get(4)).getValue();
						break;
					}
				}
			} else if(param instanceof IntParameter) {
				logger.debug("param is of IntParameter");
				IntParameter parameter = (IntParameter) param;
				if(parameter.getName().equalsIgnoreCase(AMOUNT_OF_UNITS)) {
					this.setAmountOfUnits(Long.valueOf(parameter.getValue()));
				}
			} else if(param instanceof LongParameter) {
				logger.debug("param is of LongParameter");
				LongParameter parameter = (LongParameter) param;
				if(parameter.getName().equalsIgnoreCase(AMOUNT_OF_UNITS)) {
					this.setAmountOfUnits(parameter.getValue());
				} else if(parameter.getName().equalsIgnoreCase(MESSAGE_ID)) {
					this.setMessageId(parameter.getValue());
				}
			}
		}
	}

	@Override
	public String toString() {
		return "RechargeRequest [customerId=" + customerId + ", eventName="
				+ eventName + ", amountOfUnits=" + amountOfUnits
				+ ", eventClass=" + eventClass + ", ratingInput0="
				+ ratingInput0 + ", ratingInput1=" + ratingInput1
				+ ", ratingInput2=" + ratingInput2 + ", ratingInput3="
				+ ratingInput3 + ", ratingInput4=" + ratingInput4
				+ ", messageId=" + messageId + ", eventInfo=" + eventInfo + "]";
	}
	
}

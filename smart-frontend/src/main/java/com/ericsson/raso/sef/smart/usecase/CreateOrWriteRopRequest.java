package com.ericsson.raso.sef.smart.usecase;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.BooleanParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.SymbolicParameter;

public class CreateOrWriteRopRequest extends SmartRequest {

	private String customerId;
	private int key;
	private boolean isSmsAllowed;
	private boolean isUSCAllowed;
	private boolean isGPRSUsed;
	private boolean isLastTransactionEnqUsed;
	private int languageId;
	private String activeEndDate;
	private String graceEndDate;
	private XMLGregorianCalendar preActiveEndDate;
	private String firstCallDate;
	private boolean isFirstCallPassed;
	private String lastKnownPeriod;
	private String category;
	private long messageId;

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

	public boolean getIsSmsAllowed() {
		return isSmsAllowed;
	}

	public void setIsSmsAllowed(boolean isSmsAllowed) {
		this.isSmsAllowed = isSmsAllowed;
	}

	public boolean getIsUSCAllowed() {
		return isUSCAllowed;
	}

	public void setIsUSCAllowed(boolean isUSCAllowed) {
		this.isUSCAllowed = isUSCAllowed;
	}

	public boolean getIsGPRSUsed() {
		return isGPRSUsed;
	}

	public void setIsGPRSUsed(boolean isGPRSUsed) {
		this.isGPRSUsed = isGPRSUsed;
	}

	public boolean getIsLastTransactionEnqUsed() {
		return isLastTransactionEnqUsed;
	}

	public void setIsLastTransactionEnqUsed(boolean isLastTransactionEnqUsed) {
		this.isLastTransactionEnqUsed = isLastTransactionEnqUsed;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public String getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(String activeEndDate) {
		this.activeEndDate = activeEndDate;
	}

	public String getGraceEndDate() {
		return graceEndDate;
	}

	public void setGraceEndDate(String graceEndDate) {
		this.graceEndDate = graceEndDate;
	}

	public XMLGregorianCalendar getPreActiveEndDate() {
		return preActiveEndDate;
	}

	public void setPreActiveEndDate(XMLGregorianCalendar preActiveEndDate) {
		this.preActiveEndDate = preActiveEndDate;
	}

	public String getFirstCallDate() {
		return firstCallDate;
	}

	public void setFirstCallDate(String firstCallDate) {
		this.firstCallDate = firstCallDate;
	}

	public boolean getIsFirstCallPassed() {
		return isFirstCallPassed;
	}

	public void setIsFirstCallPassed(boolean isFirstCallPassed) {
		this.isFirstCallPassed = isFirstCallPassed;
	}

	public String getLastKnownPeriod() {
		return lastKnownPeriod;
	}

	public void setLastKnownPeriod(String lastKnownPeriod) {
		this.lastKnownPeriod = lastKnownPeriod;
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

	@Override
	public void prepareRequest(Operation operation) {
		List<Object> parameters = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		for (Object param : parameters) {
			if(param instanceof StringParameter) {
				StringParameter parameter = (StringParameter) param;
				
				if (parameter.getName().equalsIgnoreCase("LastKnownPeriod")) {
					this.setLastKnownPeriod(parameter.getValue().trim());
				} else if (parameter.getName().equalsIgnoreCase("CustomerId")) {
					this.setCustomerId(parameter.getValue().trim());
				}
			} else if (param instanceof DateTimeParameter) {
				DateTimeParameter parameter = (DateTimeParameter) param;
				this.setPreActiveEndDate(parameter.getValue());
			} else if(param instanceof IntParameter) {
				IntParameter parameter = (IntParameter) param;
				if(parameter.getName().equalsIgnoreCase("Key")) {
					this.setKey(parameter.getValue());
				} else if (parameter.getName().equalsIgnoreCase("LanguageID")) {
					this.setLanguageId(parameter.getValue());
				}
			} else if (param instanceof BooleanParameter) {
				BooleanParameter parameter = (BooleanParameter) param;
				if(parameter.getName().equalsIgnoreCase("IsSmsAllowed")) {
					this.setIsSmsAllowed(parameter.isValue());
				} else if (parameter.getName().equalsIgnoreCase("IsUSCAllowed")) {
					this.setIsUSCAllowed(parameter.isValue());
				} else if (parameter.getName().equalsIgnoreCase("IsGPRSUsed")) {
					this.setIsGPRSUsed(parameter.isValue());
				} else if (parameter.getName().equalsIgnoreCase("IsLastTransactionEnqUsed")) {
					this.setIsLastTransactionEnqUsed(parameter.isValue());
				} else if (parameter.getName().equalsIgnoreCase("IsFirstCallPassed")) {
					this.setIsFirstCallPassed(parameter.isValue());
				}
			} else if(param instanceof SymbolicParameter) {
				SymbolicParameter parameter = (SymbolicParameter) param;
				this.setFirstCallDate(parameter.getValue().trim());
			} else if (param instanceof LongParameter) {
				LongParameter parameter = (LongParameter) param;
				this.setMessageId(parameter.getValue());
			} else if (param instanceof EnumerationValueParameter) {
				EnumerationValueParameter parameter = (EnumerationValueParameter) param;
				this.setCategory(parameter.getValue());
			}
		}
	}

	@Override
	public String toString() {
		return "CreateOrWriteRopRequest [customerId=" + customerId + ", key="
				+ key + ", isSmsAllowed=" + isSmsAllowed + ", isUSCAllowed="
				+ isUSCAllowed + ", isGPRSUsed=" + isGPRSUsed
				+ ", isLastTransactionEnqUsed=" + isLastTransactionEnqUsed
				+ ", languageId=" + languageId + ", activeEndDate="
				+ activeEndDate + ", graceEndDate=" + graceEndDate
				+ ", preActiveEndDate=" + preActiveEndDate + ", firstCallDate="
				+ firstCallDate + ", isFirstCallPassed=" + isFirstCallPassed
				+ ", lastKnownPeriod=" + lastKnownPeriod + ", category="
				+ category + ", messageId=" + messageId + "]";
	}

}
	
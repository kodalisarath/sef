package com.ericsson.sm.core.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.sm.core.Meta;
import com.ericsson.sm.core.db.model.ChargeAmount;
import com.ericsson.sm.core.db.model.CurrencyCode;

public abstract class NotificationEvent implements Serializable {
	
	public static final String DEFAULT_EVENTID = "0";

	private static final long serialVersionUID = 1L;
	
	private String description;
	private List<NotificationMessage> messages = new ArrayList<NotificationMessage>();
	private String senderAddr;
	
	private List<Meta> metas =  new ArrayList<Meta>();
	
	private ChargeAmount chargeAmount = new ChargeAmount(0, CurrencyCode.CENTS);

	public ChargeAmount getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(ChargeAmount chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<NotificationMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<NotificationMessage> messages) {
		this.messages = messages;
	}

	public List<Meta> getMetas() {
		return metas;
	}

	public void setMetas(List<Meta> metas) {
		this.metas = metas;
	}

	public String getSenderAddr() {
		return senderAddr;
	}

	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}

	@Override
	public String toString() {
		return "NotificationEvent [description=" + description + ", messages="
				+ messages + ", senderAddr=" + senderAddr + ", metas=" + metas
				+ ", chargeAmount=" + chargeAmount + "]";
	}

}

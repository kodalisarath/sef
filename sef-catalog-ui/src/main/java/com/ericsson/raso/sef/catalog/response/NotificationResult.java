package com.ericsson.raso.sef.catalog.response;

import java.io.Serializable;
import java.util.List;

import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.core.ne.Language;
import com.ericsson.raso.sef.core.ne.NotificationAction;

public class NotificationResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String eventId;
	
	private String description;
	
	private String wsClientId;
	
	private String senderAddr;
	
	private NotificationAction notificationAction;
	
	private long chargeAmount;
	
	private List<NotificationMessageDetail> notificationMessages ;
	
	private CurrencyCode currencyCode;
	
	private String messageId ;
	
	private String message;
	
	private Language language;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWsClientId() {
		return wsClientId;
	}

	public void setWsClientId(String wsClientId) {
		this.wsClientId = wsClientId;
	}

	public String getSenderAddr() {
		return senderAddr;
	}

	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}

	public NotificationAction getNotificationAction() {
		return notificationAction;
	}

	public void setNotificationAction(NotificationAction notificationAction) {
		this.notificationAction = notificationAction;
	}

	public long getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(long chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public List<NotificationMessageDetail> getNotificationMessages() {
		return notificationMessages;
	}

	public void setNotificationMessages(
			List<NotificationMessageDetail> notificationMessages) {
		this.notificationMessages = notificationMessages;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}
	
}
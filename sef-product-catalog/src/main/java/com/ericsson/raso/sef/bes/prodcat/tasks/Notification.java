package com.ericsson.raso.sef.bes.prodcat.tasks;

public final class Notification extends TransactionTask {
	private static final long serialVersionUID = 2993665754961050718L;

	private NotificationMode mode = null;
	private String offerId = null;
	private String subscriberId = null;

	
	public Notification(NotificationMode mode, String offerId, String subscriberId) {
		super(Type.NOTIFICATION);
		this.mode = mode;
		this.offerId = offerId;
		this.subscriberId = subscriberId;
	}


	public NotificationMode getMode() {
		return mode;
	}


	public void setMode(NotificationMode mode) {
		this.mode = mode;
	}


	public String getOfferId() {
		return offerId;
	}


	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}


	public String getSubscriberId() {
		return subscriberId;
	}


	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}


	
		
		
}

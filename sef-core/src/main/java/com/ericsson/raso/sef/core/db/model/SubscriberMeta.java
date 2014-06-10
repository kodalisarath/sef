package com.ericsson.raso.sef.core.db.model;

import java.io.Serializable;

public class SubscriberMeta implements Serializable {
	private static final long serialVersionUID = -1234592856938357485L;

	private String subscriberId = null;
	private String key = null;
	private String value = null;
	
	
	public SubscriberMeta() { }
	
	public SubscriberMeta(String subscriberId, String key, String value) {
		super();
		this.subscriberId = subscriberId;
		this.key = key;
		this.value = value;
	}
	
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "SubscriberMeta [subscriberId=" + subscriberId + ", key=" + key
				+ ", value=" + value + "]";
	}
	
	
	
	
}

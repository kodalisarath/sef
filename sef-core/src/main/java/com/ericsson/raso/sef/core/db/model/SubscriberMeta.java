package com.ericsson.raso.sef.core.db.model;

import java.io.Serializable;

import com.ericsson.raso.sef.core.Meta;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof SubscriberMeta) {

			if (getClass() != obj.getClass())
				return false;
			SubscriberMeta other = (SubscriberMeta) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (subscriberId == null) {
				if (other.subscriberId != null)
					return false;
			} else if (!subscriberId.equals(other.subscriberId))
				return false;
		}
		
		if (obj instanceof Meta) {
			Meta other = (Meta) obj;
			if (key == null) {
				if (other.getKey() != null)
					return false;
			} else if (!key.equals(other.getKey()))
				return false;
		}
		
		
		
		return true;
	}

	@Override
	public String toString() {
		return "SubscriberMeta [subscriberId=" + subscriberId + ", key=" + key
				+ ", value=" + value + "]";
	}
	
	
	
	
}

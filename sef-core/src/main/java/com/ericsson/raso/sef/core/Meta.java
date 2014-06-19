package com.ericsson.raso.sef.core;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.ericsson.raso.sef.core.db.model.SubscriberMeta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="meta")
public class Meta implements Serializable{

	@Override
	public String toString() {
		return "Meta [key=" + key + ", value=" + value + "]";
	}

	private static final long serialVersionUID = 1L;
	private String key;
	private String value;

	public Meta() {}

	public Meta(String key, String value) {
		this.key = key;
		this.value = value;
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

		if (obj instanceof Meta) {
			if (getClass() != obj.getClass())
				return false;
			Meta other = (Meta) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
		}
		
		if (obj instanceof SubscriberMeta) {
			SubscriberMeta meta = (SubscriberMeta) obj;
			if (key == null) {
				if (meta.getKey() != null)
					return false;				
			} else if (!key.equals(meta.getKey()))
				return false;
		}
		return true;
	}
	
	
}

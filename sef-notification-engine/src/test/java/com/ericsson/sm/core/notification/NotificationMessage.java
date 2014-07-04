package com.ericsson.sm.core.notification;

import java.io.Serializable;

import com.ericsson.sm.core.config.Language;

public class NotificationMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id=null;
	private Language lang;
	private String message;

	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	@Override
	public String toString() {
		return "NotificationMessage [id=" + id + ", lang=" + lang
				+ ", message=" + message + "]";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

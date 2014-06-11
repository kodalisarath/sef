package com.ericsson.raso.sef.ne.notification;

import java.io.Serializable;



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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

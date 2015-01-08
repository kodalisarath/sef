package com.ericsson.raso.sef.catalog.response;

import com.ericsson.raso.sef.core.ne.Language;

public class NotificationMessageDetail {

	private String message;
	private Language language;
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
}

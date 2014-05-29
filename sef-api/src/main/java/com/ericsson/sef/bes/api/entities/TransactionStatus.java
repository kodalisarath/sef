package com.ericsson.sef.bes.api.entities;

public class TransactionStatus {
	private String component;
	private Integer code;
	private String description;
	
	
	
	public TransactionStatus(String component, Integer code, String description) {
		this.component = component;
		this.code = code;
		this.description = description;
	}

	public String getComponent() {
		return component;
	}
	
	public void setComponent(String component) {
		this.component = component;
	}
	
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public TransactionStatus() {
		
	}

}

package com.ericsson.sef.bes.api.entities;

import java.io.Serializable;

public class TransactionStatus implements Serializable {
	private String component;
	private Integer code = 0;
	private String description;
	
	
	
	@Override
	public String toString() {
		return "TransactionStatus [component=" + component + ", code=" + code
				+ ", description=" + description + "]";
	}

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

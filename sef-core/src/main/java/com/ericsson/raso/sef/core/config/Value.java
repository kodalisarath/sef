package com.ericsson.raso.sef.core.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="values")
public class Value {
	
	private String id;
	private String sdpId;
	private String siteId;
	
	@XmlAttribute
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@XmlAttribute
	public String getSdpId() {
		return sdpId;
	}
	
	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}
	
	@XmlAttribute
	public String getSiteId() {
		return siteId;
	}
	
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	@Override
	public String toString() {
		return "Value [id=" + id + ", sdpId=" + sdpId + ", siteId=" + siteId + "]";
	}
	
	

}

package com.ericsson.raso.sef.core.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="values")
public class Value {
	
	private String id;
	private String sdpId;
	private String siteId;
	
	public String getId() {
		return id;
	}
	
	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSdpId() {
		return sdpId;
	}
	
	@XmlAttribute
	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}
	
	public String getSiteId() {
		return siteId;
	}
	
	@XmlAttribute
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	@Override
	public String toString() {
		return "Value [id=" + id + ", sdpId=" + sdpId + ", siteId=" + siteId + "]";
	}
	
	

}

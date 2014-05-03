package com.ericsson.sef.bes.api.subscription;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="scheduleRequest")
public class ScheduleRequest {
	private String requestId;
	private String price;
	private String status;
	private String productId;
	private String lifeCycleEvent;
	private Date createDate;
	private Date scheduleDate;
	
	public String getRequestId() {
		return requestId;
	}
	public String getPrice() {
		return price;
	}
	public String getStatus() {
		return status;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getLifeCycleEvent() {
		return lifeCycleEvent;
	}
	public void setLifeCycleEvent(String lifeCycleEvent) {
		this.lifeCycleEvent = lifeCycleEvent;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

}

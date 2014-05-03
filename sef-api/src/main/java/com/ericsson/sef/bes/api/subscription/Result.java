package com.ericsson.sef.bes.api.subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "purchaseResult")
public class Result {

	private String requestId;
	private Date scheduleDate;
	private int statusCode;
	private List<Meta> resultMetas;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public List<Meta> getResultMetas() {
		if(resultMetas == null) {
			resultMetas = new ArrayList<Meta>();
		}
		return resultMetas;
	}
	
	public void setResultMetas(List<Meta> resultMetas) {
		this.resultMetas = resultMetas;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
}

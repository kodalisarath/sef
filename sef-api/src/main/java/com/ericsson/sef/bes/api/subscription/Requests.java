package com.ericsson.sef.bes.api.subscription;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pendingRequest")
public class Requests {
	List<ScheduleRequest> scheduleRequests;

	public List<ScheduleRequest> getScheduleRequests() {
		return scheduleRequests;
	}

	public void setScheduleRequests(List<ScheduleRequest> scheduleRequests) {
		this.scheduleRequests = scheduleRequests;
	}

}

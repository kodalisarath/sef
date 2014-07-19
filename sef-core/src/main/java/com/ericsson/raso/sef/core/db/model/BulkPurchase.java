package com.ericsson.raso.sef.core.db.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BulkPurchase extends ConcurrentHashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void addCommerceTrail(CommerceTrail commerceTrail) {
		List<CommerceTrail> list = (List<CommerceTrail>) super.get("commerceTrail");
		if(list == null) {
			list = new ArrayList<CommerceTrail>();
			super.put("commerceTrail", list);
		}
		list.add(commerceTrail);
	}
	
	@SuppressWarnings("unchecked")
	public void addPurchase(Purchase purchase) {
		List<Purchase> list = (List<Purchase>) super.get("purchase");
		if(list == null) {
			list = new ArrayList<Purchase>();
			super.put("purchase", list);
		}
		list.add(purchase);
	}
	
	@SuppressWarnings("unchecked")
	public void addScheduledRequest(ScheduledRequest scheduledRequest) {
		List<ScheduledRequest> list = (List<ScheduledRequest>) super.get("scheduledRequest");
		if(list == null) {
			list = new ArrayList<ScheduledRequest>();
			super.put("scheduledRequest", list);
		}
		list.add(scheduledRequest);
	}
	
	@SuppressWarnings("unchecked")
	public void addScheduledRequestMeta(ScheduledRequestMeta scheduledRequestMeta) {
		List<ScheduledRequestMeta> list = (List<ScheduledRequestMeta>) super.get("scheduledRequestMeta");
		if(list == null) {
			list = new ArrayList<ScheduledRequestMeta>();
			super.put("scheduledRequestMeta", list);
		}
		list.add(scheduledRequestMeta);
	}
	
	@SuppressWarnings("unchecked")
	public void addJobDetail(JobDetail jobDetail) {
		List<JobDetail> list = (List<JobDetail>) super.get("jobDetail");
		if(list == null) {
			list = new ArrayList<JobDetail>();
			super.put("jobDetail", list);
		}
		list.add(jobDetail);
	}

}

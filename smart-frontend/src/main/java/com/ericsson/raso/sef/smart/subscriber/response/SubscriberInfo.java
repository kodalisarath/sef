package com.ericsson.raso.sef.smart.subscriber.response;

import java.util.Map;

import com.ericsson.raso.sef.core.db.model.ContractState;

public class SubscriberInfo extends AbstractSubscriberResponse {

	private String msisdn;
	private ContractState remoteState;
	private ContractState localState;
	private boolean isLocked = false;
	private Map<String,String> metas;
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public ContractState getRemoteState() {
		return remoteState;
	}
	public void setRemoteState(ContractState remoteState) {
		this.remoteState = remoteState;
	}
	public ContractState getLocalState() {
		return localState;
	}
	public void setLocalState(ContractState localState) {
		this.localState = localState;
	}
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public Map<String, String> getMetas() {
		return metas;
	}
	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}
	
	
	
}

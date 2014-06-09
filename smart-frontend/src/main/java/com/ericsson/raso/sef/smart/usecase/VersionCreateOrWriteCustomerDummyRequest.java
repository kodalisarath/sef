package com.ericsson.raso.sef.smart.usecase;

import java.util.Collection;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.ContractState;

public class VersionCreateOrWriteCustomerDummyRequest {
	private  ContractState subscriberContractState;
	private  Collection<Meta> subscriberMeta;
	public ContractState getSubscriberContractState() {
		return subscriberContractState;
	}
	public void setSubscriberContractState(ContractState subscriberContractState) {
		this.subscriberContractState = subscriberContractState;
	}
	public Collection<Meta> getSubscriberMeta() {
		return subscriberMeta;
	}
	public void setSubscriberMeta(Collection<Meta> subscriberMeta) {
		this.subscriberMeta = subscriberMeta;
	}
}

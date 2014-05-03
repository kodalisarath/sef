package com.ericsson.sef.bes.api.subscriber;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum ContractState {
	PREACTIVE, ACTIVE, RECYCLED, GRACE, READY_TO_DELETE, NONE;
}

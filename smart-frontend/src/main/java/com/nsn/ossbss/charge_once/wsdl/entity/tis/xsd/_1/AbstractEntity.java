package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEntity {
	
	protected Map<String, Object> edr = new HashMap<String, Object>();
	
	public Map<String, Object> toEdr() {
		return edr;
	}
	
	protected void addParam(String key, Object value) {
		edr.put(key, value);
	}

}

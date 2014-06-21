package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.UTF8String;

public class ErrorTextAvp extends UTF8String {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 195;
	
	public ErrorTextAvp(String value) {
		super(AVP_CODE,28458);
		setData(value);
	}
	
	@Override
	public String getName() {
		return "Error-Text";
	}

}

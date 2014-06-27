package com.ericsson.raso.sef.core.cg.nsn.avp;

import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.UTF8String;

public class AccountOwnerIdAvp extends UTF8String {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 200;
	
	public AccountOwnerIdAvp(String value) {
		super(AVP_CODE, 28458);
		setData(value);
	}
	
	@Override
	public String getName() {
		return "NSN-Account-Owner-Id";
	}
}

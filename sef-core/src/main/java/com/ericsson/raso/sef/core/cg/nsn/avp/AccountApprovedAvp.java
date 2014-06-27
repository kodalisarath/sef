package com.ericsson.raso.sef.core.cg.nsn.avp;

import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Unsigned32;

public class AccountApprovedAvp extends Unsigned32 {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 201;
	
	public AccountApprovedAvp(int value) {
		super(AVP_CODE, 28458);
		setData(value);
	}
	
	@Override
	public String getName() {
		return "NSN-Account-Approved";
	}
}

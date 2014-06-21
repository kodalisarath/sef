package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Integer32;

public class AccountTypeAvp extends Integer32 {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 199;
	
	public AccountTypeAvp(Integer value) {
		super(AVP_CODE, 28458);
		setData(value);
	}
	
	@Override
	public String getName() {
		return "NSN-Account-Type";
	}
}

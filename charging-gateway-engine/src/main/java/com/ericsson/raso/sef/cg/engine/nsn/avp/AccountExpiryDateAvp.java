package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Unsigned64;

public class AccountExpiryDateAvp extends Unsigned64 {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 205;
	
	public AccountExpiryDateAvp(Long value) {
		super(AVP_CODE, 28458);
		setValue(value);
	}
	
	@Override
	public String getName() {
		return "NSN-Account-Expiry-Date";
	}
}

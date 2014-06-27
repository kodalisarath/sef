package com.ericsson.raso.sef.core.cg.nsn.avp;

import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Unsigned64;

public class AccountCurrentBalanceAvp extends Unsigned64 {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 202;
	
	public AccountCurrentBalanceAvp(Long value) {
		super(AVP_CODE, 28458);
		setData(value);
	}
	
	@Override
	public String getName() {
		return "NSN-Account-Current-Balance";
	}
}

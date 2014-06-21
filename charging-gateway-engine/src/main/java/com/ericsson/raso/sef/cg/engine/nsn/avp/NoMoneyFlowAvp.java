package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Unsigned32;

public class NoMoneyFlowAvp extends Unsigned32 {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 191;
	
	public NoMoneyFlowAvp() {
		super(AVP_CODE,28458);
	}

	public NoMoneyFlowAvp(boolean value) {
		super(AVP_CODE,28458);
		setValue(value == true ? 1 : 0);
	}
	
	@Override
	public String getName() {
		return "No-Money-Flow";
	}

}

package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;

public class ErrorItemAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 192;

	public ErrorItemAvp() {
		super(AVP_CODE, 28458);
	}
	
	@Override
	public String getName() {
		return "Error-Item";
	}
}

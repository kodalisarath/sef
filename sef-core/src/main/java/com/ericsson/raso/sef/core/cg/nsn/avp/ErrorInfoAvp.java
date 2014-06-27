package com.ericsson.raso.sef.core.cg.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;

public class ErrorInfoAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 179;
	
	public ErrorInfoAvp() {
		super(AVP_CODE,28458);
	}
	
	@Override
	public String getName() {
		return "NSN-Error-Info";
	}
	
}

package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.Enumerated;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;

public class MethodNameAvp extends Enumerated {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 178;
	
	public MethodNameAvp(int value) {
		super(value);
	}

	public MethodNameAvp(Avp avp) {
		super(avp);
	}
}

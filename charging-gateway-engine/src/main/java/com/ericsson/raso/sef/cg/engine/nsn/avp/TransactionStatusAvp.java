package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.Enumerated;

public class TransactionStatusAvp extends Enumerated {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 188;
	
	public static final int REQUESTED = 0;
	public static final int PROCESSED = 1;
	public static final int TIMEOUT = 2;
	public static final int FAILED = 3;
	public static final int AUTHORIZED = 5;
	public static final int CAPTUREREQUESTED = 6;
	public static final int OPEN = 8;
	
	public TransactionStatusAvp(int value) {
		super(AVP_CODE, 28458);
		setData(value);
	}
	
	@Override
	public String getName() {
		return "NSN-Transaction-Status";
	}
}

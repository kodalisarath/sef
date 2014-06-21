package com.ericsson.raso.sef.cg.engine.nsn.avp.sms;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;

public class OriginatorInterfaceAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 2009;
	
	public OriginatorInterfaceAvp(Avp avp) {
		super(avp);
	}
	
	public Avp getInterfaceType() throws AvpDataException{
		Avp avp = getSubAvp(2006);
		if(avp == null) return null;
		return avp;
	}
}

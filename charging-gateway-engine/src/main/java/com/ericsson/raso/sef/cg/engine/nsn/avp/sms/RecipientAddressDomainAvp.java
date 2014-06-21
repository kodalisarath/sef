package com.ericsson.raso.sef.cg.engine.nsn.avp.sms;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;

public class RecipientAddressDomainAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 898;
	
	public RecipientAddressDomainAvp(Avp avp) {
		super(avp);
	}
	
	public Avp getDomainName() throws AvpDataException{
		Avp avp = getSubAvp(1200);
		if(avp == null) return null;
		return avp;
	}
}

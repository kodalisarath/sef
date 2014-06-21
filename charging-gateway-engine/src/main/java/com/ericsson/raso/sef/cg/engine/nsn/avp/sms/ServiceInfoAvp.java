package com.ericsson.raso.sef.cg.engine.nsn.avp.sms;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;

public class ServiceInfoAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 873;
	
	public ServiceInfoAvp(Avp avp) {
		super(avp);
	}
	
	public SMSInformationAvp getSMSInformationAvp() {
		Avp avp = getSubAvp(SMSInformationAvp.AVP_CODE);
		if(avp == null) return null;
		return new SMSInformationAvp(avp);
	}
	
	public MMSInformationAvp getMMSInformationAvp() {
		Avp avp = getSubAvp(MMSInformationAvp.AVP_CODE);
		if(avp == null) return null;
		return new MMSInformationAvp(avp);
	}
	
}

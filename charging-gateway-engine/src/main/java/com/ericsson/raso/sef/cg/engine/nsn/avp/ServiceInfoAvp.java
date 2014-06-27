package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.raso.sef.core.cg.nsn.avp.PPIInformationAvp;

public class ServiceInfoAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 873;
	
	public ServiceInfoAvp() {
		super(AVP_CODE, 10415);
	}

	public ServiceInfoAvp(Avp avp) {
		super(avp);
	}
	
	public PPIInformationAvp getPpiInformationAvp() {
		return new PPIInformationAvp(getSubAvp(PPIInformationAvp.AVP_CODE));
	}
	
	public void addPPiInformationAvp(PPIInformationAvp avp) {
		this.addSubAvp(avp);
	}
}

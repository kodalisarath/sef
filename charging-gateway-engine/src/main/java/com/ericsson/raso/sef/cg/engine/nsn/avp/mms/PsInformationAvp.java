package com.ericsson.raso.sef.cg.engine.nsn.avp.mms;

import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.core.cg.nsn.avp.mms.GPPChargingIdAvp;

public class PsInformationAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 874;
	
	public PsInformationAvp(Avp avp) {
		super(avp);
	}
	
	public ServiceParameterInfoAvp get3GPPChargingId() throws AvpDataException{
		Avp avp = this.getSubAvp(GPPChargingIdAvp.AVP_CODE);
		if(avp != null)  return new GPPChargingIdAvp(avp.getAsInt()).convertToServiceParameterInfoAvp();
		return null;
	}
	
	public Avp get3GPPUserLocationInfo() {
		Avp avp = getSubAvp(22);
		if(avp == null) return null;
		return avp;
	}
	
}

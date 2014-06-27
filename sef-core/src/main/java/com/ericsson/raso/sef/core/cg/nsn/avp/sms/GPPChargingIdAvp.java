package com.ericsson.raso.sef.core.cg.nsn.avp.sms;

import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Integer32;

public class GPPChargingIdAvp extends Integer32 {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 2;
	public static final int SERVICE_PARAMETER_TYPE = 50;
	
	public GPPChargingIdAvp(int value) {
		super(AVP_CODE);
		setData(value);
	}
	public GPPChargingIdAvp(Avp avp) {
	}
	
	public ServiceParameterInfoAvp convertToServiceParameterInfoAvp() {
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(SERVICE_PARAMETER_TYPE));
		info.addSubAvp(new ServiceParameterValueAvp(getData()));
		return info;
	}
	
	@Override
	public String getName() {
		return "3GPP-Charging-Id";
	}
	
}

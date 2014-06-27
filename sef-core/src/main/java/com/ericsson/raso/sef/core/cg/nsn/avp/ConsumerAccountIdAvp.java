package com.ericsson.raso.sef.core.cg.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Unsigned64;

public class ConsumerAccountIdAvp extends Unsigned64 {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 174;
	public static final int SERVICE_PARAMETER_TYPE = 201;
	
	public ConsumerAccountIdAvp(long value) {
		super(AVP_CODE, 28458);
		setData(value);
	}
	
	public ServiceParameterInfoAvp convertToServiceParameterInfoAvp() {
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(SERVICE_PARAMETER_TYPE));
		info.addSubAvp(new ServiceParameterValueAvp(getData()));
		return info;
	}
	
	@Override
	public String getName() {
		return "NSN-Consumer-Account-Id";
	}
}

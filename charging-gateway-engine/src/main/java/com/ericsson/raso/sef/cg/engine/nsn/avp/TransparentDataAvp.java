package com.ericsson.raso.sef.cg.engine.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.UTF8String;

public class TransparentDataAvp extends UTF8String {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 176;
	public static final int SERVICE_PARAMETER_TYPE = 205;

	public TransparentDataAvp(String value) {
		super(AVP_CODE, 28458);
		setData(value);
	}

	public TransparentDataAvp() {
		super(AVP_CODE, 28458);
	}


	public ServiceParameterInfoAvp convertToServiceParameterInfoAvp() {
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(SERVICE_PARAMETER_TYPE));
		info.addSubAvp(new ServiceParameterValueAvp(getData()));
		return info;
	}

	@Override
	public String getName() {
		return "NSN-Transparent-Data";
	}
}

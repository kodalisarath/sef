package com.ericsson.raso.sef.core.cg.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.UTF8String;

public class ProductIdAvp extends UTF8String {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 170;
	public static final int SERVICE_PARAMETER_TYPE = 208;
	
	public ProductIdAvp(String value) {
		super(AVP_CODE, 28458);
		setValue(value);
	}
	
	public ServiceParameterInfoAvp convertToServiceParameterInfoAvp() throws AvpDataException {
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(SERVICE_PARAMETER_TYPE));
		
		ServiceParameterValueAvp serviceParameterValueAvp = new ServiceParameterValueAvp();
		serviceParameterValueAvp.setData(getData());
		info.addSubAvp(serviceParameterValueAvp);
		return info;
	}
	
	@Override
	public String getName() {
		return "NSN-Product-Id";
	}
}

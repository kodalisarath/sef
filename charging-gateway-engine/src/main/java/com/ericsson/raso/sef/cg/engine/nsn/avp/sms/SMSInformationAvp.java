package com.ericsson.raso.sef.cg.engine.nsn.avp.sms;

import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.raso.sef.core.cg.nsn.avp.sms.OriginatorInterfaceAvp;

public class SMSInformationAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 2000;

	public SMSInformationAvp(Avp avp) {
		super(avp);
	}


	public String getOriginatorSCCPAddress() throws AvpDataException {
		Avp avp = getSubAvp(2008); 
		if(avp == null) return null;
		return avp.getAsUTF8String().trim();
	}

	public ServiceParameterInfoAvp getSMSCAddress() {
		Avp avp = getSubAvp(2017);
		if(avp ==null) return null;
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(254));
		info.addSubAvp(new ServiceParameterValueAvp(avp.getData()));
		return info;
	}

	public ServiceParameterInfoAvp getSMMessageType() {
		Avp avp = getSubAvp(2007);
		if(avp ==null) return null;
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(53));
		info.addSubAvp(new ServiceParameterValueAvp(avp.getData()));
		return info;
	}

	public ServiceParameterInfoAvp getOriginatorInterfaceType() throws AvpDataException {
		Avp avp = getSubAvp(OriginatorInterfaceAvp.AVP_CODE);
		if(avp ==null) return null;
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(54));
		OriginatorInterfaceAvp oiAvp = new OriginatorInterfaceAvp(avp);
		info.addSubAvp(new ServiceParameterValueAvp(oiAvp.getInterfaceType().getData()));
		return info;
	}
}

package com.ericsson.raso.sef.core.cg.nsn.avp.sms;

import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;

public class AdressAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int ORIGINATOR_ADDRESS_AVP_CODE = 886;
	public static final int RECIPIENT_ADDRESS_AVP_CODE = 1201;

	public AdressAvp(Avp avp) {
		super(avp);
	}

	public ServiceParameterInfoAvp getOriginAddressTypeAvp() {
		Avp avp = getSubAvp(899);
		if(avp == null) return null;
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(52));
		info.addSubAvp(new ServiceParameterValueAvp(avp.getData()));
		return info;
	}

	public ServiceParameterInfoAvp getOriginAdressDataAvp() {
		Avp avp = getSubAvp(897);
		if(avp == null) return null;
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(252));
		info.addSubAvp(new ServiceParameterValueAvp(avp.getData()));
		return info;
	}

	public ServiceParameterInfoAvp getRecipientAddressTypeAvp() {
		Avp avp = getSubAvp(899);
		if(avp == null) return null;
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(51));
		info.addSubAvp(new ServiceParameterValueAvp(avp.getData()));
		return info;
	}

	public ServiceParameterInfoAvp getRecipientAdressDataAvp() {
		Avp avp = getSubAvp(897);
		if(avp == null) return null;
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(251));
		info.addSubAvp(new ServiceParameterValueAvp(avp.getData()));
		return info;
	}

}

package com.ericsson.raso.sef.cg.engine.nsn.avp.mms;

import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.raso.sef.core.cg.nsn.avp.mms.AdressAvp;

public class MMSInformationAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 877;

	public MMSInformationAvp(Avp avp) {
		super(avp);
	}

	public AdressAvp getOriginatorAddressAvp() {
		Avp avp = getSubAvp(AdressAvp.ORIGINATOR_ADDRESS_AVP_CODE);
		if(avp == null) return null;

		return new AdressAvp(avp);
	}

	public AdressAvp getRecipientAddressAvp() {
		Avp avp = getSubAvp(AdressAvp.RECIPIENT_ADDRESS_AVP_CODE);
		if(avp == null) return null;
		return new AdressAvp(avp);
	}

	public Avp geMessageIdAvp() {
		Avp avp = getSubAvp(1210);
		if(avp == null) return null;
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		info.addSubAvp(new ServiceParameterTypeAvp(254));
		info.addSubAvp(new ServiceParameterValueAvp(avp.getData()));
		return info;
	}

	public Avp getMessageTypeAvp() {
		Avp avp = getSubAvp(1211);
		if(avp == null) return null;
		return avp;
	}

	public Avp getMessageSizeAvp() {
		Avp avp = getSubAvp(1212);
		if(avp == null) return null;
		return avp;
	}

}

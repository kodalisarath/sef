package com.ericsson.raso.sef.cg.engine.nsn.avp;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;

public class PPIInformationAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;
	
	public static final int AVP_CODE = 102;

	public PPIInformationAvp() {
		super(AVP_CODE, 28458);
	}

	public PPIInformationAvp(Avp avp) {
		super(avp);
	}
	
	public void addTransparentData(String value) {
		this.addSubAvp(new TransparentDataAvp(value));
	}
	
	public void addProductId(String value) {
		this.addSubAvp(new ProductIdAvp(value));
	}
	
	public void addPurpose(String value) {
		this.addSubAvp(new PurposeAvp(value));
	}
	
	public void addMerchantId(String value) {
		this.addSubAvp(new MerchantIdAvp(value));
	}
	
	public void addConsumerAccountId(long value) {
		this.addSubAvp(new ConsumerAccountIdAvp(value));
	}
	
	public void addAccessFrontendId(String value) {
		this.addSubAvp(new AccessFrontendIdAvp(value));
	}
	
	public void addCurrency(String value) {
		this.addSubAvp(new CurrencyAvp(value));
	}
	
	public void addMethodName(int value) {
		this.addSubAvp(new MethodNameAvp(value));
	}
	
	public AccessFrontendIdAvp getAccessFrontendIdAvp() throws AvpDataException {
		Avp avp = this.getSubAvp(AccessFrontendIdAvp.AVP_CODE);
		if(avp != null)  return new AccessFrontendIdAvp(avp.getAsUTF8String());
		return null;
	}
	
	
	public ConsumerAccountIdAvp getConsumerAccountIdAvp() throws AvpDataException {
		Avp avp = this.getSubAvp(ConsumerAccountIdAvp.AVP_CODE);
		if (avp != null) return new ConsumerAccountIdAvp(avp.getAsLong());
		return null;
	}
	
	public List<ConsumerAccountIdAvp> getConsumerAccountIdAvps() throws AvpDataException {
		List<Avp> avps = this.getAllSubAvp(ConsumerAccountIdAvp.AVP_CODE);
		List<ConsumerAccountIdAvp> accountIdAvps = new ArrayList<ConsumerAccountIdAvp>();
		if (avps != null) {
			for (Avp avp : avps) {
				ConsumerAccountIdAvp accountIdAvp = new ConsumerAccountIdAvp(avp.getAsLong());
				accountIdAvps.add(accountIdAvp);
			}
		}
		return accountIdAvps;
	}
	
	public CurrencyAvp getCurrencyAvp() throws AvpDataException {
		Avp avp = this.getSubAvp(CurrencyAvp.AVP_CODE);
		if(avp!=null) return new CurrencyAvp(avp.getAsUTF8String());
		return null;
	}
	
	public MerchantIdAvp getMerchantIdAvp() throws AvpDataException {
		Avp avp = this.getSubAvp(MerchantIdAvp.AVP_CODE);
		if(avp != null) return new MerchantIdAvp(avp.getAsUTF8String());
		return null;
	}
	
	public ProductIdAvp getProductdIdAvp() throws AvpDataException {
		Avp avp = this.getSubAvp(ProductIdAvp.AVP_CODE);
		if(avp != null) return new ProductIdAvp(avp.getAsUTF8String());
		return null;
	}
	
	public PurposeAvp getPurposeAvp() throws AvpDataException {
		Avp avp = this.getSubAvp(PurposeAvp.AVP_CODE);
		if(avp != null) return new PurposeAvp(avp.getAsUTF8String());
		return null;
	}
	
	public TransparentDataAvp getTransparentData() throws AvpDataException {
		Avp avp = this.getSubAvp(TransparentDataAvp.AVP_CODE);
		if(avp != null) return new TransparentDataAvp(avp.getAsUTF8String());
		return null;
	}
	
	public MethodNameAvp getMethodNameAvp() throws AvpDataException  {
		Avp avp = this.getSubAvp(MethodNameAvp.AVP_CODE);
		if(avp != null) return new MethodNameAvp(avp);
		return null;
	}
	
	@Override
	public String getName() {
		return "NSN-PPI-Information";
	}
}

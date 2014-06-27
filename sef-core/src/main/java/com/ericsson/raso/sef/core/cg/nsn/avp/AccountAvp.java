package com.ericsson.raso.sef.core.cg.nsn.avp;

import com.ericsson.pps.diameter.dccapi.avp.avpdatatypes.DccGrouped;
import com.ericsson.pps.diameter.rfcapi.base.avp.ResultCodeAvp;

public class AccountAvp extends DccGrouped {

	private static final long serialVersionUID = 1L;

	public static final int AVP_CODE = 186;

	public AccountAvp() {
		super(AVP_CODE, 28458);
	}

	public void addAccountId(Long value) {
		this.addSubAvp(new AccountIdAvp(value));
	}

	public void addAccountCurrentBalance(Long value) {
		this.addSubAvp(new AccountCurrentBalanceAvp(value));
	}
	public void addAccountCurrentAuthorizedAmount(Long value) {
		this.addSubAvp(new AccountCurrentAuthorizedAmount(value));
	}
	public void addAccountLastBalanceModDate(Long value) {
		this.addSubAvp(new AccountLastBalanceModDate(value));
	}
	
	public void addAccountExpiryDate(Long value) {
		this.addSubAvp(new AccountExpiryDateAvp(value));
	}
	public void addAccountType(Integer value) {
		this.addSubAvp(new AccountTypeAvp(value));
	}
	public void addAccountApproved(int value) {
		this.addSubAvp(new AccountApprovedAvp(value));
	}
	public void addAccountOwnerId(String value) {
		this.addSubAvp(new AccountOwnerIdAvp(value));
	}
	public void addCurreny(String value) {
		this.addSubAvp(new CurrencyAvp(value));
	}

	public void addResultCode(int value) {
		this.addSubAvp(new ResultCodeAvp(value));
	}
	
	@Override
	public String getName() {
		return "NSN-Account";
	}


}

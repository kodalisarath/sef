package com.ericsson.raso.sef.smart.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SmartModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long transactionAmount;
	
	
	private List<AccountInfo> accountInfos;
	
	private List<RechargeBalance> rechargeBalances;
	
	public List<AccountInfo> getAccountInfos() {
		if(accountInfos == null)  {
			accountInfos = new ArrayList<AccountInfo>();
		}
		return accountInfos;
	}

	public void setAccountInfos(List<AccountInfo> accountInfos) {
		this.accountInfos = accountInfos;
	}

	public List<RechargeBalance> getRechargeBalances() {
		if(rechargeBalances == null) {
			rechargeBalances = new ArrayList<RechargeBalance>();
		}
		return rechargeBalances;
	}

	public void setRechargeBalances(List<RechargeBalance> rechargeBalances) {
		this.rechargeBalances = rechargeBalances;
	}

	public long getTransactionAmount() {
		return transactionAmount;
	}
	
	public void setTransactionAmount(long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
}

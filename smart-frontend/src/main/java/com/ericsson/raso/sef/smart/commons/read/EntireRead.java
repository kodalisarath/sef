package com.ericsson.raso.sef.smart.commons.read;

import java.util.ArrayList;
import java.util.Collection;

public class EntireRead {
	
	@Override
	public String toString() {
		return "EntireRead [customer=" + customer + ", rop=" + rop
				+ ", welcomePack=" + welcomePack + ", rpps=" + rpps + "]";
	}

	private Customer customer;
	private Rop rop;
	private WelcomePack welcomePack;
	private Collection<Rpp> rpps = new ArrayList<Rpp>();

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Rop getRop() {
		return rop;
	}

	public void setRop(Rop rop) {
		this.rop = rop;
	}

	public Collection<Rpp> getRpps() {
		return rpps;
	}

	public void setRpps(Collection<Rpp> rpps) {
		this.rpps = rpps;
	}

	public WelcomePack getWelcomePack() {
		return welcomePack;
	}

	public void setWelcomePack(WelcomePack welcomePack) {
		this.welcomePack = welcomePack;
	}
}

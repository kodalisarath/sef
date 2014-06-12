package com.ericsson.raso.sef.smart;

import java.util.Set;
import java.util.TreeSet;

public class PasaloadRule {

	private Integer id = null;
	private String name = null;
	private Integer aPartyCountThreshold = null;
	private Integer aPartyAmountThreshold = null;
	private Integer bPartyCountThreshold = null;
	private Integer bPartyAmountThreshold = null;
	
	
	public PasaloadRule() {
		
	}
	
	public static Set<PasaloadRule> parseConfiguration(String configuration) {
		Set<PasaloadRule> pasaRules = new TreeSet<PasaloadRule>();
		
		/*
		 * Sample configuration looks like this....
		 * id=pasa15,name=Pasaload(15PHP),aPartyCountThreshold=5,aPartyAmountThreshold=65,bPartyCountThreshold=10,bPartyAmountThreshold=30; 
		 * id=pasa45,name=Pasaload(45PHP),aPartyCountThreshold=2,aPartyAmountThreshold=90,bPartyCountThreshold=3,bPartyAmountThreshold=30;
		 */
		
		String pasaloads[] = configuration.split(";");
		for (String pasaload: pasaloads) {
			
		}
		
		
		return pasaRules;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getaPartyCountThreshold() {
		return aPartyCountThreshold;
	}

	public void setaPartyCountThreshold(Integer aPartyCountThreshold) {
		this.aPartyCountThreshold = aPartyCountThreshold;
	}

	public Integer getaPartyAmountThreshold() {
		return aPartyAmountThreshold;
	}

	public void setaPartyAmountThreshold(Integer aPartyAmountThreshold) {
		this.aPartyAmountThreshold = aPartyAmountThreshold;
	}

	public Integer getbPartyCountThreshold() {
		return bPartyCountThreshold;
	}

	public void setbPartyCountThreshold(Integer bPartyCountThreshold) {
		this.bPartyCountThreshold = bPartyCountThreshold;
	}

	public Integer getbPartyAmountThreshold() {
		return bPartyAmountThreshold;
	}

	public void setbPartyAmountThreshold(Integer bPartyAmountThreshold) {
		this.bPartyAmountThreshold = bPartyAmountThreshold;
	}

	
	
	
	
	
	
	
}

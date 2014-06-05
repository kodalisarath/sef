package com.ericsson.raso.sef.fulfillment.profiles;

import java.io.Serializable;
import java.util.List;

import com.ericsson.raso.sef.client.air.request.PamInformation;

public class PamInformationList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5105163328860847633L;
	private List<PamInformation> pamInfolist;

	public List<PamInformation> getPamInfolist() {
		return pamInfolist;
	}

	public void setPamInfolist(List<PamInformation> pamInfolist) {
		this.pamInfolist = pamInfolist;
	}
	
}

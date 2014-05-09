package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.Date;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class TerminateHardDate extends AbstractAutoTermination {
	private static final long serialVersionUID = -315675038228769117L;

	private long date = -1;

	public TerminateHardDate( Date date) {
		super(Type.HARD_STOP);
		this.date = date.getTime();
	}

	@Override
	public long getTerminationTime(Subscription subsription) throws CatalogException {
		return this.date;

	}

}

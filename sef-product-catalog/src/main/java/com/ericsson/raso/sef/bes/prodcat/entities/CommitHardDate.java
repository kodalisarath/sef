package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.Date;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class CommitHardDate extends AbstractMinimumCommitment {
	private static final long serialVersionUID = -858943043023615771L;

	private long date = -1;

	public CommitHardDate( Date date) {
		super(Type.HARD_LIMIT);
		this.date = date.getTime();
	}

	@Override
	public long getCommitmentTime(Subscription subsription) throws CatalogException {
		return this.date;
	}
	
	

}

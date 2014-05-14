package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.tasks.GetActiveSubscriptionCountForOffer;
import com.ericsson.raso.sef.core.FrameworkException;

public final class AccumulateNotAllowed extends AbstractAccumulationPolicy implements Serializable {
	
	public AccumulateNotAllowed(Offer offer) {
		super(offer);
	}

	private static final long serialVersionUID = 6705544103267417362L;

	@Override
	public boolean execute() {
		// allow super to enforce resource level max quota enforcement...
		if (super.execute()) {
			// check if there is any active subscription for the same offer... then reject
			try {
				Integer subscriptionCount  = new GetActiveSubscriptionCountForOffer(this.getOffer().getName(), 0).execute();
				if (subscriptionCount > 0)
					return false;
			} catch (FrameworkException e) {
				// TODO logger - what the hell happened here? write it down....
				return false; //reject anyway... I am not in mood for generosity
			}
		}
		return false; // what? the super rejected already isnt it?
	}

	
}
